package allclear.crawl;

import allclear.domain.member.Member;
import allclear.domain.subject.Subject;
import allclear.global.exception.GlobalException;
import allclear.global.exception.code.GlobalErrorCode;
import allclear.repository.subject.SubjectRepository;
import com.google.common.annotations.GwtIncompatible;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.K;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.KeysetScrollPosition;

import java.util.ArrayList;
import java.util.Arrays;

import static org.openqa.selenium.Keys.PAGE_DOWN;

public class CrawlSubjectInfo {
    @Getter
    private ArrayList<Subject> subjects = new ArrayList<>(); // subject 객체들 저장
    @Getter
    private ArrayList<String> majorSubjects = new ArrayList<>(); // 학부 전공 과목
    @Getter
    private ArrayList<String> requiredGeneralSubjects = new ArrayList<>(); // 교양 필수 과목
    @Getter
    private ArrayList<String> optionalGeneralSubjects = new ArrayList<>(); // 교양 선택 과목
    @Getter
    private ArrayList<String> chapelSubjects = new ArrayList<>(); // 채플 과목
    @Getter
    private ArrayList<String> teachingSubjects = new ArrayList<>(); // 교직 이수 과목

    WebDriver driver; // 웹 드라이버
    WebElement target; // 크롤링 할 요소
    String targetPath; // 크롤링 할 요소가 있는 경로
    String targetText; // 크롤링 할 문자

    public CrawlSubjectInfo(String usaintId, String usaintPassword) throws GlobalException {
        loginUsaint(usaintId, usaintPassword); // 로그인
        setYearSemester(2024, "1"); // 조회 연도 및 학기 설정
        //crawlMajorSubjects(); // 학부 전공 크롤링
        //crawlRequiredGeneralSubjects(); // 교양 필수 크롤링
        //crawlOptionalGeneralSubjects(); // 교양 선택 크롤링
        //crawlChapelSubjects(); // 채플 크롤링
        crawlTeachingSubjects(); // 교직 크롤링

        //subjects.addAll(ParsingSubject.parsingSubjectString(majorSubjects)); // 학부 전공 과목 파싱 후반환
        //subjects.addAll(ParsingSubject.parsingSubjectString(requiredGeneralSubjects)); // 교양 필수 과목 파싱 후 반환
        //subjects.addAll(ParsingSubject.parsingSubjectString(optionalGeneralSubjects)); // 교양 선택 과목 파싱 후반환
        //subjects.addAll(ParsingSubject.parsingSubjectString(chapelSubjects)); // 채플 과목 파싱 후 반환
        subjects.addAll(ParsingSubject.parsingSubjectString(teachingSubjects)); // 교직 과목 파싱 후반환

    }

    public void loginUsaint(String usaintId, String usaintPassword) { // 유세인트 로그인 함수
        System.setProperty("ENCODING", "UTF-8");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        // 창 숨기는 옵션 추가
        options.addArguments("window-size=1920,1000");
        options.addArguments("headless");
        options.addArguments("disable-gpu");
        options.addArguments("ignore-certificate-errors");
        options.addArguments("lang=ko");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.20 Safari/537.36");

        // 로그인 페이지 주소
        String loginUrl = "https://smartid.ssu.ac.kr/Symtra_sso/smln.asp?apiReturnUrl=https%3A%2F%2Fsaint.ssu.ac.kr%2FwebSSO%2Fsso.jsp";

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        /*
         *사용자 정보 획득을 위한 로그인
         */
        driver.get(loginUrl); // 로그인 접속
        WebElement memberNameElement = driver.findElement(By.name("userid"));
        WebElement passwordElement = driver.findElement(By.name("pwd")); // 로그인 시도
        memberNameElement.sendKeys(usaintId);
        passwordElement.sendKeys(usaintPassword);
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"sLogin\"]/div/div[1]/form/div/div[2]/a"));
        loginButton.click();
        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            WebElement homeButton = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/header/div[2]/div[1]/ul/li[1]"));
            homeButton.click();
            // 로그인이 정상적으로 완료되었는지 확인하기 위해 홈버튼 클릭
            // 클릭이 안 될 경우 예외 처리
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorCode._USAINT_LOGIN_FAILED);
        }

        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void setYearSemester(int year, String semester){ // 해당 년도와 학기에 해당하는 과목 요소 크롤링
        Actions scroll = new Actions(driver); // 콤보박스 스크롤하기 위한 요소
        int firstYear; // 학년도 콤보박스에서 자동으로 선택되는 학년도, 유세인트 업데이트 시마다 수정됨
        int count = 0; // 콤보박스 이동 횟수 초기화

        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement managementButton = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/header/div[2]/div[1]/ul/li[3]"));
        managementButton.click(); // 학사 관리 클릭
        WebElement requestButton = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/header/div[2]/div[1]/ul/li[3]/div/ul/li[2]/a"));
        requestButton.click(); // 수강신청/교과과정 클릭

        try {
            Thread.sleep(5000); // 5초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // iframe 이동
        WebElement iframe1 = driver.findElement(By.xpath("//*[@id=\"contentAreaFrame\"]"));
        driver.switchTo().frame(iframe1);
        WebElement iframe2 = driver.findElement(By.xpath("//*[@id=\"isolatedWorkArea\"]"));
        driver.switchTo().frame(iframe2);
        // 크롤링 환경 셋팅 끝

        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // firstYear 설정
        target = driver.findElement(By.id("WD89")); // 자동 선택되어진 학년도 추출
        firstYear = Integer.parseInt(target.getAttribute("value").substring(0,4));
        // 조회 학년도 선택
        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[2]/span/span"));
        target.click();
        for (int i = 0; i < year - firstYear; i++) {  // 학년도 콤보박스에서 조회 해당 년도로 이동
            scroll.sendKeys(Keys.ARROW_DOWN).perform();
        }
        scroll.click().perform();

        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 조회 학기 선택
        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[2]/td/div/table/tbody/tr/td[5]/span"));
        target.click();
        scroll.sendKeys(Keys.PAGE_UP).perform();
        // 조회 학기에 따른 콤보박스 이동횟수 설정
        if (semester.equals("여름")) count = 1;
        else if (semester.equals("2")) count = 2;
        else if (semester.equals("겨울")) count = 3;
        for(int i=0;i<count;i++){
            scroll.sendKeys(Keys.ARROW_DOWN).perform();
        }
        scroll.click().perform();

        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void crawlMajorSubjects(){ // 학부전공별 과목 크롤링 환경 설정
        int majorUniversityCount = 11; // 단과대학 개수
        int majorCount = 0;
        Actions scroll = new Actions(driver); // 콤보박스 스크롤하기 위한 요소

        for(int i=0;i<majorUniversityCount;i++){ // 단과대학 개수만큼 반복, 현재 11개의 단과대학 존재, 유세인트 수정 시 변경 필요
            target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/" +
                    "table/tbody/tr[3]/td/table/tbody/tr[3]/td/div[1]/div/div/span/span/table/" +
                    "tbody/tr/td/table/tbody/tr/td[2]"));
            target.click(); // 단과대학 콤보박스 클릭
            scroll.sendKeys(Keys.ARROW_DOWN).perform();
            scroll.click().perform();
            try {
                Thread.sleep(1000); // 1초 동안 실행을 멈추기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            switch (i){ // 단과대학별 학과 개수 설정
                case 0 : majorCount = 12; break; // 인문대학, 스포츠학부 추가 검색 필요
                case 1 : majorCount = 5; break; // 자연과학대학
                case 2 : majorCount = 2; break; // 법과대학
                case 3 : majorCount = 6; break; // 사회과학대학
                case 4 : majorCount = 5; break; // 경제통상대학
                case 5 : majorCount = 8; break; // 경영대학
                case 6 : majorCount = 6; break; // 공과대학, 건축학부 추가 검색 필요
                case 7 : majorCount = 9; break; // IT대학, 정보통신 전자공학부 추가 검색 필요
                case 8 : majorCount = 0; break; // 베어드 교양대학
                case 9 : majorCount = 1; break; // 융합특성자유전공학부
                case 10 : majorCount = 1; break; // 차세대반도체학과
            }

            for (int j=0;j<majorCount;j++){
                target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[3]/td/div[1]/div/div/span/span/table/tbody/tr/td/table/tbody/tr/td[3]"));
                target.click(); // 학과 콤보박스 클릭
                scroll.sendKeys(Keys.ARROW_DOWN).perform();
                scroll.click().perform();
                try {
                    Thread.sleep(1000); // 1초 동안 실행을 멈추기
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i == 0 && j == 11){
                    for(int k=0;k < 3;k++){ // 스포츠학부, 생활체육전공, 스포츠 사이언스
                        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[3]/td/div[1]/div/div/span/span/table/tbody/tr/td/table/tbody/tr/td[4]"));
                        target.click();
                        scroll.sendKeys(Keys.ARROW_DOWN).perform();
                        scroll.click().perform();
                        try {
                            Thread.sleep(1000); // 1초 동안 실행을 멈추기
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[3]/td/div[1]/div/div/span/span/table/tbody/tr/td/table/tbody/tr/td[5]"));
                        target.click(); // 검색 클릭
                        crawlTable(1,5);
                    }
                }
                else if (i == 6 && j == 5){
                    for(int k=0;k < 4;k++){ // 건축학부, 건축공학전공, 건축학 전공, 실내건축 전공
                        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[3]/td/div[1]/div/div/span/span/table/tbody/tr/td/table/tbody/tr/td[4]"));
                        target.click();
                        scroll.sendKeys(Keys.ARROW_DOWN).perform();
                        scroll.click().perform();
                        try {
                            Thread.sleep(1000); // 1초 동안 실행을 멈추기
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[3]/td/div[1]/div/div/span/span/table/tbody/tr/td/table/tbody/tr/td[5]"));
                        target.click(); // 검색 클릭
                        crawlTable(1,5);
                    }
                }
                else if (i == 7 && j == 4){
                    for(int k=0;k < 2;k++){ // 정보통신전자공학부, 전자공학과
                        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[3]/td/div[1]/div/div/span/span/table/tbody/tr/td/table/tbody/tr/td[4]"));
                        target.click();
                        scroll.sendKeys(Keys.ARROW_DOWN).perform();
                        scroll.click().perform();
                        try {
                            Thread.sleep(1000); // 1초 동안 실행을 멈추기
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[3]/td/div[1]/div/div/span/span/table/tbody/tr/td/table/tbody/tr/td[5]"));
                        target.click(); // 검색 클릭
                        crawlTable(1,5);
                    }
                }
                else{
                    try {
                        Thread.sleep(1000); // 1초 동안 실행을 멈추기
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[3]/td/div[1]/div/div/span/span/table/tbody/tr/td/table/tbody/tr/td[5]"));
                    target.click(); // 검색 클릭
                    crawlTable(1,5);
                }
            }
        }

    }

    public void crawlRequiredGeneralSubjects(){ // 교양필수 크롤링
        Actions scroll = new Actions(driver); // 콤보박스 스크롤하기 위한 요소
        int subjectCount = 34; // 교양필수 과목 개수, 현재 34개의 교양필수 과목 존재, 변경 시 수정 필요

        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[1]/td/table/tbody/tr/td[2]/div/div[2]"));
        target.click(); // 교양필수 클릭

        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[3]/td/div[2]/div/div/table/tbody/tr/td[4]"));
        target.click(); // 콤보박스 클릭
        scroll.sendKeys(Keys.PAGE_UP).perform(); // 콤보박스 상단으로 이동

        for(int i=0;i<subjectCount;i++){
            if (i != 0){
                target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[3]/td/div[2]/div/div/table/tbody/tr/td[4]"));
                target.click(); // 콤보박스 클릭
                scroll.sendKeys(Keys.ARROW_DOWN).perform();
            }
            try {
                Thread.sleep(1000); // 1초 동안 실행을 멈추기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[3]/td/div[2]/div/div/table/tbody/tr/td[5]"));
            target.click(); // 검색버튼 클릭
            crawlTable(2,5);
        }
    }

    public void crawlOptionalGeneralSubjects(){ // 교양선택 크롤링
        Actions scroll = new Actions(driver); // 콤보박스 스크롤하기 위한 요소

        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[1]/td/table/tbody/tr/td[2]/div/div[3]"));
        target.click(); // 교양선택 클릭

        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[3]/td/div[3]/div/div/div/table/tbody/tr/td/div/div/table/tbody/tr/td[2]"));
        target.click(); // 콤보박스 클릭
        scroll.sendKeys(Keys.PAGE_UP).perform(); // 콤보박스 상단으로 이동
        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[3]/td/div[3]/div/div/div/table/tbody/tr/td/div/div/table/tbody/tr/td[3]"));
        target.click(); // 검색버튼 클릭
        crawlTable(3,7);
    }

    public void crawlChapelSubjects(){ // 채플 크롤링
        Actions scroll = new Actions(driver); // 콤보박스 스크롤하기 위한 요소
        int subjectCount = 3; // 채플 과목 개수, 현재 3개의 채플 과목 존재, 변경 시 수정 필요

        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[1]/td/table/tbody/tr/td[2]/div/div[4]"));
        target.click(); // 채플 클릭

        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[3]/td/div[4]/div/div/table/tbody/tr/td[4]"));
        target.click(); // 콤보박스 클릭
        scroll.sendKeys(Keys.PAGE_UP).perform(); // 콤보박스 상단으로 이동
        for(int i=0;i<subjectCount;i++){
            if (i != 0){
                target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[3]/td/div[4]/div/div/table/tbody/tr/td[4]"));
                target.click(); // 콤보박스 클릭
                scroll.sendKeys(Keys.ARROW_DOWN).perform();
            }
            try {
                Thread.sleep(1000); // 1초 동안 실행을 멈추기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[3]/td/div[4]/div/div/table/tbody/tr/td[5]"));
            target.click(); // 검색버튼 클릭
            crawlTable(4,1);
        }
    }

    public void crawlTeachingSubjects(){ // 교직 크롤링
        Actions scroll = new Actions(driver); // 콤보박스 스크롤하기 위한 요소

        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[1]/td/table/tbody/tr/td[2]/div/div[5]"));
        target.click(); // 교직 클릭
        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기, 실행 안 멈출 시 오류 발생
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[3]/td/div[5]/div/div"));
        target.click(); // 검색 클릭
        crawlTable(5,1);
    }

    public void crawlTable(int subjectFlag, int timeSleepTime){ // 테이블 크롤링 수행
        Actions scroll = new Actions(driver); // 콤보박스 스크롤하기 위한 요소

        try {
            Thread.sleep(1000L * timeSleepTime); // timeSleepTime 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[5]/td/table/tbody/tr/td/table/tbody/tr[1]/td/div/table/tbody/tr/td[1]/div/table/tbody/tr/td[2]"));
        target.click(); // 줄수/페이지 버튼 클릭
        scroll.sendKeys(Keys.PAGE_DOWN).perform();
        scroll.click().perform(); // 500줄로 변경

        try {
            Thread.sleep(3000); // 3초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int tr = 2; // 행
        while(true){
            try {
                int td; // 열
                for(td=2;td<=15;td++){
                    if (td == 5 || td == 9 || td == 12 || td == 13) // 크롤링 불필요 요소
                        continue;
                    else if(td == 6){ // 과목번호 크롤링
                        targetPath = "/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[5]/td/table/tbody/tr/td/table/tbody/tr[2]/td/div/div/div/span/span/table/tbody/tr[2]/td/div/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[" +
                                tr + "]/td[" + td + "]/a/span[1]";
                    }
                    else{
                        targetPath = "/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[5]/td/table/tbody/tr/td/table/tbody/tr[2]/td/div/div/div/span/span/table/tbody/tr[2]/td/div/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[" +
                                tr + "]/td[" + td +"]/span/span[1]";
                    }
                    target = driver.findElement(By.xpath(targetPath));
                    targetText = target.getText();
                    switch (subjectFlag){ // 플래그 값에 따른 리스트 추가
                        case 1 : majorSubjects.add(targetText);break;
                        case 2 : requiredGeneralSubjects.add(targetText);break;
                        case 3 : optionalGeneralSubjects.add(targetText);break;
                        case 4 : chapelSubjects.add(targetText);break;
                        case 5: teachingSubjects.add(targetText);break;
                    }

                }
                tr++;
            }
            catch (Exception e){ // 검색할게 없는 경우
                break;
            }
        }
    }
    public void closeDriver() {
        driver.quit();
    }
}
