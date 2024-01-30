package allclear.crawl;

import allclear.domain.grade.Grade;
import allclear.domain.member.Member;
import allclear.domain.requirement.Requirement;
import allclear.global.exception.GlobalException;
import allclear.global.exception.code.GlobalErrorCode;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;

@Getter
public class CrawlMemberInfo {

    @Getter
    private Member member;
    @Getter
    private Requirement requirement;
    @Getter
    private Grade grade;

    private ArrayList<String> requirementComponentList = new ArrayList<>();
    private ArrayList<String> entireGrades = new ArrayList<>();  //  전체 성적 리스트
    private ArrayList<String> detailGrades = new ArrayList<>(); // 학기별 세부 성적 리스트
    private String firstYear; // 최초 학년
    private String firstSemester; // 최초 학기
    private String totalCredit; // 총 이수 학점
    private String averageGrade; // 평균 학점
    WebDriver driver;

    public CrawlMemberInfo(String usaintId, String usaintPassword){

        member = new Member();

        // 로그인
        try {
            loginUsaint(usaintId, usaintPassword);
        } catch (GlobalException e) {
            throw e;
        } catch (Exception e){
            throw new GlobalException(GlobalErrorCode._USAINT_UNAVAILABLE);
        }

        try {
            // 크롤링
            crawlMemberComponent();
            crawlRequirementComponent();
            crawlEntireGrades();
            crawlDetailGrades();
        }
        catch (Exception e){
            throw new GlobalException(GlobalErrorCode._USAINT_CRAWLING_FAILED);
        }

        if(requirementComponentList == null){
            throw new GlobalException(GlobalErrorCode._USAINT_PARSING_FAILED); // 파싱 실패
        }
        if(totalCredit == null || averageGrade == null || entireGrades == null || detailGrades == null){
            throw new GlobalException(GlobalErrorCode._USAINT_PARSING_FAILED); // 파싱 실패
        }

        try {
            requirement = ParsingRequirement.parsingRequirementString(requirementComponentList);
            grade = ParsingGrade.parsingGradeString(totalCredit, averageGrade, entireGrades, detailGrades);
        }
        catch (Exception e){
            throw new GlobalException(GlobalErrorCode._USAINT_PARSING_FAILED); // 파싱 실패
        }
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
        // 입력 받은 아이디와 비밀번호

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

    public void crawlMemberComponent() { // 사용자 정보 크롤링 함수
        WebElement target;

        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement managementButton = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/header/div[2]/div[1]/ul/li[3]"));
        managementButton.click();
        try {
            Thread.sleep(2000); // 2초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //프레임 이동
        WebElement iframe1Element = driver.findElement(By.name("contentAreaFrame"));
        driver.switchTo().frame(iframe1Element);
        WebElement iframe2Element = driver.findElement(By.name("isolatedWorkArea"));
        driver.switchTo().frame(iframe2Element);
        // memberName 크롤링
        try {
            Thread.sleep(5000); // 5초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // user_name 크롤링
        target = driver.findElement(By.id("WDC9"));
        member.setUsername(target.getAttribute("value"));

        // university 크롤링
        target = driver.findElement(By.id("WDBB"));
        member.setUniversity(target.getAttribute("value"));

        // major 크롤링
        target = driver.findElement(By.id("WDC4"));
        member.setMajor(target.getAttribute("value"));

        // mail 크롤링
        target = driver.findElement(By.id("WDF6"));
        member.setEmail(target.getAttribute("value"));

        // classType 크롤링
        target = driver.findElement(By.id("WDD6"));
        member.setClassType(target.getAttribute("value"));

        // year 크롤링
        target = driver.findElement(By.id("WDE1"));
        member.setLevel(Integer.parseInt(target.getAttribute("value").strip()));

        // semester 크롤링
        target = driver.findElement(By.id("WDE5"));
        member.setSemester(Integer.parseInt(target.getAttribute("value").strip()));

        // 기본 프레임으로 돌아가기
        driver.switchTo().defaultContent();
    }

    public void crawlRequirementComponent() { // 졸업요건 조회 크롤링 함수
        // 성적/졸업 버튼 클릭
        WebElement gradeAndGraduationButton = driver.findElement(By.xpath("//*[@id=\"8d3da4feb86b681d72f267880ae8cef5\"]"));
        gradeAndGraduationButton.click();
        Duration timeout = Duration.ofSeconds(10);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        // 졸업사정표 버튼 클릭
        WebElement graduationRequirementButton = driver.findElement(By.xpath("//*[@id=\"30f2303171c98bdf57db799d0b834646\"]/a"));
        graduationRequirementButton.click();
        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement iframe1Element = driver.findElement(By.name("contentAreaFrame"));
        driver.switchTo().frame(iframe1Element);
        WebElement iframe2Element = driver.findElement(By.name("isolatedWorkArea"));
        driver.switchTo().frame(iframe2Element);
        try {
            Thread.sleep(5000); // 5초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*
         * 졸업요건 정보 획득
         */
        int exit_flag = 0;
        String targetRoot;
        int i = 2;
        WebElement target;
        String text;
        while (true) {
            for (int j = 1; j <= 6; j++) {
                targetRoot = "/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[4]/td/" +
                        "table/tbody[2]/tr/td/div/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[" + i + "]/td[" + j + "]";
                try {
                    target = driver.findElement(By.xpath(targetRoot));
                    text = target.getText().strip();
                    if (text.equals("채플"))
                        exit_flag = 1;
                    requirementComponentList.add(text);
                } catch (Exception e) {
                    continue;
                }
            }
            if (exit_flag == 1)
                break;
            i = i + 1;
        }
        // 기본 프레임 이동
        driver.switchTo().defaultContent();
        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 전체 성적 조회 함수
    public void crawlEntireGrades() {

        // 추출 할 xpath 경로를 가지는 문자열
        String targetPath;
        // 추출 한 타겟
        WebElement target;
        // 타겟 문자
        String targetText;

        // 반복문 위한 cnt;
        int cnt = 1;

        // 기본 frame으로 frame 재설정
        // driver.switchTo().defaultContent();

        // 학기별 성적 조회 클릭
        WebElement gradeCheckBtn = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[1]/div[2]/ul/li[1]/a"));
        gradeCheckBtn.click();

        try {
            Thread.sleep(7000); // 7초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // web frame 변경
        WebElement iframe1 = driver.findElement(By.xpath("//*[@id=\"contentAreaFrame\"]"));
        driver.switchTo().frame(iframe1);

        WebElement iframe2 = driver.findElement(By.xpath("//*[@id=\"isolatedWorkArea\"]"));
        driver.switchTo().frame(iframe2);

        try {
            Thread.sleep(5000); // 5초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 총 신청 학점
        target = driver.findElement(By.id("WD0151"));
        totalCredit = target.getAttribute("value");


        // 전체 평균 학점
        target = driver.findElement(By.id("WD015E"));
        averageGrade = target.getAttribute("value");

        // 팝업 창 닫기 클릭
        try{
            WebElement iframe3 = driver.findElement(By.xpath("//*[@id=\"URLSPW-0\"]"));
            driver.switchTo().frame(iframe3);

            try {
                Thread.sleep(3000); // 3초 동안 실행을 멈추기
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }


            WebElement closePopUpBtn = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/div[1]/div/div[1]/table/tbody/tr/td[3]/a"));
            closePopUpBtn.click();

            // 기본 frame으로 frame 재설정
            driver.switchTo().defaultContent();

            try {
                Thread.sleep(5000); // 5초 동안 실행을 멈추기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // web frame 변경
            driver.switchTo().frame(iframe1);
            driver.switchTo().frame(iframe2);
        }catch(Exception e){
            // 기본 frame으로 frame 재설정
            driver.switchTo().defaultContent();

            try {
                Thread.sleep(5000); // 5초 동안 실행을 멈추기
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            // web frame 변경
            driver.switchTo().frame(iframe1);
            driver.switchTo().frame(iframe2);
        }

        Boolean endFlag = false;

        while (true) {
            for (int i = 2; i <= 14; i++) {
                try {
                    if (cnt == 1) // 도메인 저장된 경로 필요없을시 cnt를 2부터 시작하면 됨.
                        targetPath = "/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[5]/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr[" + cnt + "]/th[" + i + "]";
                    else // 실제 필요한 정보들의 경로
                        targetPath = "/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[5]/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr[" + cnt + "]/td[" + i + "]";
                    target = driver.findElement(By.xpath(targetPath));
                    targetText = target.getText().strip();

                    entireGrades.add(targetText);

                    // 최초 학년도랑 최초 학기 저장.
                    if (i == 2 && !targetText.isEmpty()) {
                        firstYear = targetText.concat("학년도");
                    }
                    if (i == 3 && !targetText.isEmpty())
                        firstSemester = targetText;
                } catch (Exception e) // 요소가 더 이상 없을 때 까지 반복하기 때문에 예외처리 해줌
                {
                    endFlag = true;
                    break;
                }
            }

            if (endFlag) {
                break;
            }

            cnt++;
        }
    }

    // 학기별 세부 성적 크롤링
    public void crawlDetailGrades() {
        String selectedYear; // 현재 선택된 학년 ex) 2023학년도
        String selectedSemester; // 현재 선택된 학기 ex) 2 학기
        WebElement prevBtn = null; // 이전 학기 버튼
        String targetPath; // 크롤링 할 요소가 있는 경로
        WebElement target; // 크롤링 할 요소
        String targetText; // 크롤링 할 문자

        // 크롤링 환경 셋팅 시작

        // 기본 프레임 이동
        driver.switchTo().defaultContent();

        try {
            Thread.sleep(7000); // 7초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // iframe 들
        WebElement iframe1 = driver.findElement(By.xpath("//*[@id=\"contentAreaFrame\"]"));
        driver.switchTo().frame(iframe1);
        WebElement iframe2 = driver.findElement(By.xpath("//*[@id=\"isolatedWorkArea\"]"));
        driver.switchTo().frame(iframe2);
        // 크롤링 환경 셋팅 끝

        // 세부 성적 크롤링
        while (true) {
            int cnt = 2;
            Boolean inEndFlag = false;
            int yearFlag = 0; // 학년도 충족 플래그
            int semesterFlag = 0; // 학기 충족 플래그

            // 현재 선택된 학년도 추출
            targetPath = "/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[11]/td/table/tbody/tr/td/table/tbody/tr/td[3]/div/table/tbody/tr/td[2]/span/input";
            target = driver.findElement(By.xpath(targetPath));
            targetText = target.getAttribute("value").strip();
            selectedYear = targetText;

            // 현재 선택된 학기 추출
            targetPath = "/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[11]/td/table/tbody/tr/td/table/tbody/tr/td[3]/div/table/tbody/tr/td[5]/span/input";
            target = driver.findElement(By.xpath(targetPath));
            targetText = target.getAttribute("value").strip();
            selectedSemester = targetText;

            detailGrades.add("*"+selectedYear+ " "+selectedSemester); //구분해주는용

            // 플래그 설정 ( 성적을 끝까지 크롤링 했는지 확인 하기 위함 )
            if (firstYear.equals(selectedYear))
                yearFlag = 1;
            if (firstSemester.equals(selectedSemester))
                semesterFlag = 1;

            // 학기별 세부 성적 크롤링
            while (true) {
                for (int i = 2; i <= 9; i++) {
                    if(i==5)
                        continue;
                    try{
                        if (cnt == 1)
                            targetPath = "/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[12]/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr[" + cnt + "]/th[" + i + "]/div/div/span/span";
                        else
                            targetPath = "/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[12]/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr[" + cnt + "]/td[" + i + "]/span/span[1]";

                        target = driver.findElement(By.xpath(targetPath));
                        targetText = target.getText().strip();
                        detailGrades.add(targetText);
                    } catch (Exception e) {
                        inEndFlag = true;
                        break;
                    }
                }
                if (inEndFlag) {
                    break;
                }
                cnt++;
            }

            // 최초 학년도 최초 학기 크롤링 까지 끝냈을 시 반복문 종료
            if (yearFlag == 1 && semesterFlag == 1)
                break;

            prevBtn = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[11]/td/table/tbody/tr/td/table/tbody/tr/td[3]/div/table/tbody/tr/td[7]/div"));
            // 이전 학기 버튼 클릭
            prevBtn.click();

            try {
                Thread.sleep(3000); // 3초 동안 실행을 멈추기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



            // 버튼 클릭시 팝업 처리 로직 시작

//            try {
//                WebElement iframe3 = driver.findElement(By.xpath("//*[@id=\"URLSPW-0\"]"));
//
//                // 버튼 클릭시 팝업 처리 로직 시작
//                driver.switchTo().frame(iframe3);
//                WebElement closeBtn = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/div[1]/div/div[1]/table/tbody/tr/td[3]/a"));
//                closeBtn.click();
//
//                // 기본 프레임 이동
//                driver.switchTo().defaultContent();
//
//                try {
//                    Thread.sleep(7000); // 7초 동안 실행을 멈추기
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    Thread.sleep(3000); // 3초 동안 실행을 멈추기
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                driver.switchTo().frame(iframe1);
//                driver.switchTo().frame(iframe2);
//            } catch (Exception e) {
//                // 기본 프레임 이동
//                driver.switchTo().defaultContent();
//
//                try {
//                    Thread.sleep(7000); // 7초 동안 실행을 멈추기
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//
//                driver.switchTo().frame(iframe1);
//                driver.switchTo().frame(iframe2);
//            }
//            // 버튼 클릭시 팝업 처리 로직 끝
        }
    }

    // 웹 드라이버 닫는 함수
    public void closeDriver() {
        driver.quit();
    }
}