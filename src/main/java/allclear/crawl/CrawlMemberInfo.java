package allclear.crawl;

import allclear.domain.grade.Grade;
import allclear.domain.member.Member;
import allclear.domain.requirement.Requirement;
import allclear.global.exception.GlobalException;
import allclear.global.exception.code.GlobalErrorCode;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CrawlMemberInfo {

    @Getter
    private Member member;
    @Getter
    private Requirement requirement;
    @Getter
    private Grade grade;
    @Getter
    private List<Long> prevSubjectIdList = new ArrayList<>(); // 수강한 과목 리스트
    @Getter
    private List<Long> curriculumSubjectIdList = new ArrayList<>(); // 전공 교과 과정 리스트

    private ArrayList<String> requirementComponentList = new ArrayList<>();
    private ArrayList<String> entireGrades = new ArrayList<>();  //  전체 성적 리스트
    private ArrayList<String> detailGrades = new ArrayList<>(); // 학기별 세부 성적 리스트
    private String firstYear; // 최초 학년
    private String firstSemester; // 최초 학기
    private String totalCredit; // 총 이수 학점
    private String averageGrade; // 평균 학점
    private Integer enterYear; // 입학 연도
    private String university; // 대학
    private String major; // 학부
    private String detailMajor; // 전공
    WebElement target; // 크롤링 할 요소
    String targetPath; // 크롤링 할 요소가 있는 경로
    String targetText; // 크롤링 할 문자
    WebDriver driver;

    public CrawlMemberInfo(String usaintId, String usaintPassword) {


        // 로그인
        try {
            loginUsaint(usaintId, usaintPassword);
        } catch (GlobalException e) {
            throw e;
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorCode._USAINT_UNAVAILABLE);
        }

        //유저 크롤링
        try {
            crawlMemberComponent();
            crawlCurriculumSubject(enterYear, university, major, detailMajor);
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorCode._USAINT_USER_CRAWLING_FAILED);
        }
        try {
            prevSubjectIdList = ParsingGrade.prevSubjectIdList;
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorCode._USER_PARSING_FAILED);
        }

        //졸업요건 크롤링
        try {
            crawlRequirementComponent();
            if (requirementComponentList == null)
                throw new GlobalException(GlobalErrorCode._USAINT_REQUIREMENT_CRAWLING_FAILED); // 파싱 실패

        } catch (Exception e) {
            throw new GlobalException(GlobalErrorCode._USAINT_REQUIREMENT_CRAWLING_FAILED);
        }
        try {
            requirement = ParsingRequirement.parsingRequirementString(requirementComponentList);
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorCode._REQUIREMENT_PARSING_FAILED);
        }

        //성적 크롤링
        try {
            crawlEntireGrades();
            crawlDetailGrades();
            if (totalCredit == null || averageGrade == null || entireGrades == null || detailGrades == null)
                throw new GlobalException(GlobalErrorCode._USAINT_GRADE_CRAWLING_FAILED); // 파싱 실패
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorCode._USAINT_GRADE_CRAWLING_FAILED);
        }
        try {
            grade = ParsingGrade.parsingGradeString(totalCredit, averageGrade, entireGrades, detailGrades);
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorCode._GRADE_PARSING_FAILED);
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
        String user_name = target.getAttribute("value");
//        member.setUsername(target.getAttribute("value"));

        // university 크롤링
        target = driver.findElement(By.id("WDBB"));
        university = target.getAttribute("value");
//        member.setUniversity(target.getAttribute("value"));
//        university = member.getUniversity();

        // major 크롤링
        target = driver.findElement(By.id("WDC4"));
        major = target.getAttribute("value");
//        member.setMajor(target.getAttribute("value"));
//        major = member.getMajor();

        // mail 크롤링
        target = driver.findElement(By.id("WDF6"));
        String mail = target.getAttribute("value");
//        member.setEmail(target.getAttribute("value"));

        // classType 크롤링
        target = driver.findElement(By.id("WDD6"));
        String classType = target.getAttribute("value");
//        member.setClassType(target.getAttribute("value"));

        // year 크롤링
        target = driver.findElement(By.id("WDE1"));
        int year = Integer.parseInt(target.getAttribute("value").strip());
//        member.setLevel(Integer.parseInt(target.getAttribute("value").strip()));

        // semester 크롤링
        target = driver.findElement(By.id("WDE5"));
        int semester = Integer.parseInt(target.getAttribute("value").strip());
//        member.setSemester(Integer.parseInt(target.getAttribute("value").strip()));

        // 입학 연도 크롤링
        target = driver.findElement(By.id("WDB7"));
        enterYear = Integer.parseInt(target.getAttribute("value").strip());
        // 전공 크롤링
        target = driver.findElement(By.id("WDCD"));
        if (target == null)
            detailMajor = null;
        else
            detailMajor = target.getAttribute("value").strip();

        member = Member.builder()
                .username(user_name)
                .university(university)
                .major(major)
                .email(mail)
                .classType(classType)
                .level(year)
                .semester(semester)
                .build();

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
        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[7]/td/table/tbody/tr/td/table/tbody/tr[1]/td[2]/span/input"));
        totalCredit = target.getAttribute("value");


        // 전체 평균 학점
        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[7]/td/table/tbody/tr/td/table/tbody/tr[2]/td[2]/span/input"));
        averageGrade = target.getAttribute("value");

        // 팝업 창 닫기 클릭
        try {
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
        } catch (Exception e) {
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

            detailGrades.add("*" + selectedYear + " " + selectedSemester); //구분해주는용
            // 플래그 설정 ( 성적을 끝까지 크롤링 했는지 확인 하기 위함 )
            if (firstYear.equals(selectedYear))
                yearFlag = 1;
            if (firstSemester.equals(selectedSemester))
                semesterFlag = 1;

            // 학기별 세부 성적 크롤링
            while (true) {
                for (int i = 2; i <= 9; i++) {
                    if (i == 5 || i == 8) //  크롤링 안 되는 열 예외 처리
                        continue;
                    try {
                        targetPath = "/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[12]/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr[" + cnt + "]/td[" + i + "]/span/span";
                        target = driver.findElement(By.xpath(targetPath));
                        targetText = target.getText().strip();
                    } catch (Exception e) {
                        if (i == 2) {
                            inEndFlag = true;
                            break;
                        } else { // 중간 빈 열로 발생하는 오류
                            targetText = "";
                        }
                    }
                    detailGrades.add(targetText);
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
        }
        driver.switchTo().defaultContent();
    }

    // 교과 과정별 과목 Id 크롤링
    public void crawlCurriculumSubject(Integer enterYear, String university, String major, String detailMajor) {
        Actions scroll = new Actions(driver); // 콤보박스 스크롤하기 위한 요소
        int firstYear; // 학년도 콤보박스에서 자동으로 선택되는 학년도, 유세인트 업데이트 시마다 수정됨

        target = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/header/div[2]/div[1]/ul/li[3]"));
        target.click(); // 학사 관리 클릭
        target = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/header/div[2]/div[1]/ul/li[3]/div/ul/li[2]/a"));
        target.click(); // 수강신청/교과과정 클릭
        target = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[1]/div[2]/ul/li[9]/a"));
        target.click(); // 입학연도 별 교과과정 클릭

        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // iframe 이동
        WebElement iframe1 = driver.findElement(By.xpath("//*[@id=\"contentAreaFrame\"]"));
        driver.switchTo().frame(iframe1);
        WebElement iframe2 = driver.findElement(By.xpath("//*[@id=\"isolatedWorkArea\"]"));
        driver.switchTo().frame(iframe2);
        // 크롤링 환경 셋팅 끝

        // 입학 년도 설정
        target = driver.findElement(By.id("WD58"));
        firstYear = Integer.parseInt(target.getAttribute("value").substring(0, 4)); // 자동 선택 학년도 추출
        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr[1]/td/table/tbody/tr/td/table/tbody/tr[1]/td/div/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[1]/input"));
        target.click();
        for (int i = 0; i < firstYear - enterYear; i++) { // 조회 입학 연도 설정
            scroll.sendKeys(Keys.ARROW_UP).perform();
        }
        scroll.click().perform();

        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 단과 대학 설정
        target = driver.findElement(By.id("WD75"));
        target.click();
        scroll.sendKeys(Keys.PAGE_UP).perform();
        while (true) {
            if (target.getAttribute("value").equals(university)) {
                scroll.click().perform();
                break;
            }
            scroll.sendKeys(Keys.ARROW_DOWN).perform();
        }

        try {
            Thread.sleep(500); // 0.5초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 학과 설정
        target = driver.findElement(By.id("WD8A"));
        target.click();
        scroll.sendKeys(Keys.PAGE_UP).perform();
        while (true) {
            if (target.getAttribute("value").equals(major)) {
                scroll.click().perform();
                break;
            }
            scroll.sendKeys(Keys.ARROW_DOWN).perform();
        }

        try {
            Thread.sleep(500); // 0.5초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 전공 설정
        if (major.equals("건축학부")) { // 건축학부 경우 전공 존재
            target = driver.findElement(By.id("WD97"));
            target.click();
            scroll.sendKeys(Keys.PAGE_UP).perform();
            while (true) {
                if (target.getAttribute("value").equals(detailMajor)) {
                    scroll.click().perform();
                    break;
                }
                scroll.sendKeys(Keys.ARROW_DOWN).perform();
            }
        }

        try {
            Thread.sleep(500); // 0.5초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr[3]/td/table/tbody/tr/td/table/tbody/tr/td/div/div/div[2]/span[1]"));
        target.click(); // 검색 클릭

        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 팝업 창 닫기
        WebElement iframe3Element = driver.findElement(By.name("URLSPW-0"));
        driver.switchTo().frame(iframe3Element);
        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/div[1]/div/div[4]/div/table/tbody/tr/td[3]/table/tbody/tr/td/div"));
        target.click(); // 팝업 창 닫기 클릭

        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 프레임 이동
        driver.switchTo().defaultContent();
        driver.switchTo().frame(iframe1);
        driver.switchTo().frame(iframe2);

        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean exitFlag = false; // 반복문 종료 플래그
        int tr = 1; // 행

        while (true) {
            try {
                target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[2]/td/div/table/tbody/tr[3]/td/table/tbody/tr/td/table/tbody/tr[2]/td/div/table/tbody/tr/td/table/tbody[1]/tr[2]/td[1]/div/div[2]/table/tbody[1]/tr[" + tr + "]/td[4]/div/a/span"));
                exitFlag = false; // 예외가 발생하지 않으면 종료 플래그를 false 설정
                targetText = target.getText();
                curriculumSubjectIdList.add(Long.parseLong(targetText));
            } catch (Exception e) {
                if (exitFlag)
                    break;

                try {
                    Thread.sleep(500); // 0.5초 동안 실행을 멈추기
                } catch (InterruptedException interruptedException) {
                }

                target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[2]/td/div/table/tbody/tr[3]/td/table/tbody/tr/td/table/tbody/tr[2]/td/div/table/tbody/tr/td/table/tbody[1]/tr[2]/td[1]/div/div[2]/table/tbody[1]"));
                target.click();
                scroll.sendKeys(Keys.PAGE_DOWN).perform();
                exitFlag = true;

                try {
                    Thread.sleep(500); // 0.5초 동안 실행을 멈추기
                } catch (InterruptedException interruptedException) {
                }

                tr--; // 예외로 인해 크롤링 하지 못한 부분 다시 재시도
            }
            tr++;
        }
    }

    // 웹 드라이버 닫는 함수
    public void closeDriver() {
        driver.quit();
    }
}
