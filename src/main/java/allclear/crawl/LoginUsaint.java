package allclear.crawl;

import allclear.global.exception.GlobalException;
import allclear.global.exception.code.GlobalErrorCode;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginUsaint {

    @Getter
    WebDriver driver;

    public LoginUsaint(String usaintId, String usaintPassword) {

        //로그인
        try {
            loginUsaint(usaintId, usaintPassword);
        }
        catch (Exception e) {
            throw new GlobalException(GlobalErrorCode._USAINT_UNAVAILABLE);
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

        // Selenium 로거의 레벨 설정
        Logger logger = Logger.getLogger("org.openqa.selenium");
        logger.setLevel(Level.OFF);

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        //사용자 정보 획득을 위한 로그인
        driver.get(loginUrl); // 로그인 접속
        WebElement memberNameElement = driver.findElement(By.name("userid")); // id 설정
        WebElement passwordElement = driver.findElement(By.name("pwd")); // password 설정
        memberNameElement.sendKeys(usaintId); // id 입력
        passwordElement.sendKeys(usaintPassword); // password 입력
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"sLogin\"]/div/div[1]/form/div/div[2]/a"));
        loginButton.click(); // 로그인 버튼 클릭

        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            WebElement homeButton = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/header/div[2]/div[1]/ul/li[1]"));
            homeButton.click(); // 로그인이 정상적으로 완료되었는지 확인하기 위해 홈버튼 클릭, 클릭이 안 될 경우 예외 처리
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorCode._USAINT_LOGIN_FAILED);
        }

        try {
            Thread.sleep(3000); // 3초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
