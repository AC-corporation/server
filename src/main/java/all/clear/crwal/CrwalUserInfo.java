package all.clear.crwal;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.ArrayList;

@Setter
@Getter
public class CrwalUserInfo {
    private String userName;
    private String university;
    private String major;
    private String mail;
    private String classType; //분반
    private int year; //학년
    private int semester; //학기
    private ArrayList<String> requirementComponentList = new ArrayList<>();
    WebDriver driver;

    public void loginUsaint(String usaintId, String usaintPassword){ // 유세인트 로그인 함수
        System.setProperty("ENCODING", "UTF-8");
        WebDriverManager.chromedriver().setup();
        // 로그인 페이지 주소
        String loginUrl = "https://smartid.ssu.ac.kr/Symtra_sso/smln.asp?apiReturnUrl=https%3A%2F%2Fsaint.ssu.ac.kr%2FwebSSO%2Fsso.jsp";
        // 입력 받은 아이디와 비밀번호
        String userId = usaintId;
        String password = usaintPassword;

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        /*
         *사용자 정보 획득을 위한 로그인
         */
        driver.get(loginUrl); // 로그인 접속
        WebElement usernameElement = driver.findElement(By.name("userid"));
        WebElement passwordElement = driver.findElement(By.name("pwd")); // 로그인 시도
        usernameElement.sendKeys(userId);
        passwordElement.sendKeys(password);
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"sLogin\"]/div/div[1]/form/div/div[2]/a"));
        loginButton.click();
        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 학사관리 클릭
        WebElement degreeManageButton = driver.findElement(By.xpath("//*[@id=\"ddba4fb5fbc996006194d3c0c0aea5c4\"]/a"));
        degreeManageButton.click();
        crwalUserComponent();
        crwalRequirementComponent();
    }

    public void crwalUserComponent(){ // 사용자 정보 크롤링 함수
        WebElement target;
        try {
            Thread.sleep(2000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //프레임 이동
        WebElement iframe1Element = driver.findElement(By.name("contentAreaFrame"));
        driver.switchTo().frame(iframe1Element);
        WebElement iframe2Element = driver.findElement(By.name("isolatedWorkArea"));
        driver.switchTo().frame(iframe2Element);
        // username 크롤링
        target = driver.findElement(By.id("WDA1"));
        userName = target.getAttribute("value");
        // university 크롤링
        target = driver.findElement(By.id("WD93"));
        university = target.getAttribute("value");
        // major 크롤링
        target = driver.findElement(By.id("WD9C"));
        major = target.getAttribute("value");
        // mail 크롤링
        target = driver.findElement(By.id("WDCE"));
        mail =  target.getAttribute("value");
        // classType 크롤링
        target = driver.findElement(By.id("WDAE"));
        classType = target.getAttribute("value");
        // year 크롤링
        target = driver.findElement(By.id("WDB9"));
        year = Integer.parseInt(target.getAttribute("value").strip());
        // semester 크롤링
        target = driver.findElement(By.id("WDBD"));
        semester = Integer.parseInt(target.getAttribute("value").strip());
        // 기본 프레임으로 돌아가기
        driver.switchTo().defaultContent();
    }
    public void crwalRequirementComponent(){ // 졸업요건 조회 크롤링 함수
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
            Thread.sleep(5000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*
         * 졸업요건 정보 획득
         */
        int exit_flag = 0;
        int i = 2;
        String targetRoot;
        WebElement target;
        String text;
        while (true){
            for (int j=1;j<=6;j++){
                targetRoot = "/html/body/table/tbody/tr/td/div/table/tbody/tr/td/div/table/tbody/tr[3]/td/" +
                        "table/tbody[2]/tr/td/div/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr["+i+"]/td["+j+"]";
                try {
                    target = driver.findElement(By.xpath(targetRoot));
                    text = target.getText().strip();
                    if (text.equals("채플"))
                        exit_flag = 1;
                    requirementComponentList.add(text);
                }
                catch (Exception e) {
                    continue;
                }
            }
            if(exit_flag ==1)
                break;
            i = i+1;
        }
    }
}

