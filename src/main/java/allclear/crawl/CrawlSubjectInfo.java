package allclear.crawl;

import allclear.domain.member.Member;
import allclear.global.exception.GlobalException;
import allclear.global.exception.code.GlobalErrorCode;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import org.checkerframework.checker.units.qual.K;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import java.util.ArrayList;
import java.util.Arrays;

public class CrawlSubjectInfo {
    @Getter
    private ArrayList<String> subjects = new ArrayList<>();
    WebDriver driver;
    WebElement target; // 크롤링 할 요소
    String targetPath; // 크롤링 할 요소가 있는 경로
    String targetText; // 크롤링 할 문자

    public CrawlSubjectInfo(String usaintId, String usaintPassword) throws GlobalException {
        // 로그인
        loginUsaint(usaintId, usaintPassword);
        setYearSemester(2024, "1");
        firstCrawlMajorSubjects();
    }

    public void loginUsaint(String usaintId, String usaintPassword) { // 유세인트 로그인 함수
        System.setProperty("ENCODING", "UTF-8");
        WebDriverManager.chromedriver().setup();
        // 로그인 페이지 주소
        String loginUrl = "https://smartid.ssu.ac.kr/Symtra_sso/smln.asp?apiReturnUrl=https%3A%2F%2Fsaint.ssu.ac.kr%2FwebSSO%2Fsso.jsp";
        // 입력 받은 아이디와 비밀번호

        driver = new ChromeDriver();
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
        int firstYear = 2023; // 학년도 콤보박스에서 자동으로 선택되는 학년도, 유세인트 업데이트 시마다 수정 필요
        int cnt = 0; // 콤보박스 이동 횟수 초기화

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
        if (semester.equals("1")) cnt = 0;
        else if (semester.equals("여름")) cnt = 1;
        else if (semester.equals("2")) cnt = 2;
        else if (semester.equals("겨울")) cnt = 3;
        for(int i=0;i<cnt;i++){
            scroll.sendKeys(Keys.ARROW_DOWN).perform();
        }
        scroll.click().perform();
        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void firstCrawlMajorSubjects(){ // 학부전공별 과목 크롤링 환경 설정
        int majorCount = 0;
        Actions scroll = new Actions(driver); // 콤보박스 스크롤하기 위한 요소

        for(int i=0;i<11;i++){ // 단과대학 개수만큼 반복, 현재 11개의 단과대학 존재
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
                        secondCrawlMajorSubjects(); // 검색 클릭 후 크롤링 수행
                    }
                }
                else if (i == 6 && j == 5){
                    for(int k=0;k < 4;k++){ // 건축학부, 건축공학전공, 건축학 전공, 실내건축 전공
                        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[3]/td/div[1]/div/div/span/span/table/tbody/tr/td/table/tbody/tr/td[4]"));
                        target.click();
                        scroll.sendKeys(Keys.ARROW_DOWN).perform();
                        scroll.click().perform();
                        secondCrawlMajorSubjects(); // 검색 클릭 후 크롤링 수행
                    }
                }
                else if (i == 7 && j == 4){
                    for(int k=0;k < 2;k++){ // 정보통신전자공학부, 전자공학과
                        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[3]/td/div[1]/div/div/span/span/table/tbody/tr/td/table/tbody/tr/td[4]"));
                        target.click();
                        scroll.sendKeys(Keys.ARROW_DOWN).perform();
                        scroll.click().perform();
                        secondCrawlMajorSubjects(); // 검색 클릭 후 크롤링 수행
                    }
                }
                else{
                    secondCrawlMajorSubjects(); // 검색 클릭 후 크롤링 수행
                }
            }

        }

    }

    public void secondCrawlMajorSubjects(){ // 학부 전공별 과목 크롤링 수행
        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr[3]/td/div[1]/div/div/span/span/table/tbody/tr/td/table/tbody/tr/td[5]"));
        target.click(); // 검색 클릭 후 크롤링 수행
        try {
            Thread.sleep(5000); // 5초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        target = driver.findElement(By.id("WD010B"));
        System.out.println("*******" + target.getAttribute("value") + "*******"); // 학과 출력
        int tr = 2; // 행
        while(true){
            try {
                targetPath = "/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[5]/td/table/tbody/tr/td/table/tbody/tr[2]/td/div/div/div/span/span/table/tbody/tr[2]/td/div/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[" +
                        tr + "]/td[7]/span/span[1]";
                target = driver.findElement(By.xpath(targetPath));
                targetText = target.getText();
                System.out.println(targetText); // 과목명 출력
                targetPath = "/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[5]/td/table/tbody/tr/td/table/tbody/tr[2]/td/div/div/div/span/span/table/tbody/tr[2]/td/div/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[" +
                        tr + "]/td[9]/span/span[1]";
                target = driver.findElement(By.xpath(targetPath));
                targetText = target.getText();
                //System.out.println(targetText); // 교수명 출력
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
