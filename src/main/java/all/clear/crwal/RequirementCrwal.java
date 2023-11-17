package all.clear.crwal;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class RequirementCrwal {

    ArrayList<String> requirementComponentList = new ArrayList<>();

    public void crwalRequirementComponent(String usaintId, String usaintPassword){
        System.setProperty("ENCODING", "UTF-8");
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        // 유세인트 홈페이지 주소와 로그인 페이지 주소
        String loginUrl = "https://smartid.ssu.ac.kr/Symtra_sso/smln.asp?apiReturnUrl=https%3A%2F%2Fsaint.ssu.ac.kr%2FwebSSO%2Fsso.jsp";
        // 입력 받은 아이디와 비밀번호
        String username = usaintId;
        String password = usaintPassword;

        /*
         *사용자 정보 획득을 위한 로그인
         */
        driver.get(loginUrl); // 로그인 접속
        WebElement usernameElement = driver.findElement(By.name("userid"));
        WebElement passwordElement = driver.findElement(By.name("pwd")); // 로그인 시도
        usernameElement.sendKeys(username);
        passwordElement.sendKeys(password);
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"sLogin\"]/div/div[1]/form/div/div[2]/a"));
        loginButton.click();
        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*
         * 졸업사정 페이지로 이동
         */
        WebElement button = driver.findElement(By.xpath("//*[@id=\"ddba4fb5fbc996006194d3c0c0aea5c4\"]/a"));
        button.click();
        WebElement gradeElement = driver.findElement(By.xpath("//*[@id=\"8d3da4feb86b681d72f267880ae8cef5\"]"));
        gradeElement.click();
        Duration timeout = Duration.ofSeconds(10);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        WebElement requirementElement = driver.findElement(By.xpath("//*[@id=\"30f2303171c98bdf57db799d0b834646\"]/a"));
        requirementElement.click();
        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement iframeElement = driver.findElement(By.name("contentAreaFrame"));
        driver.switchTo().frame(iframeElement);
        WebElement iframe2Element = driver.findElement(By.xpath("//*[@id=\"isolatedWorkArea\"]"));
        driver.switchTo().frame(iframe2Element);
        /*
         * 졸업요건 정보 획득
         */
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        // 테이블의 display-none 목록을 모두 block으로 바꾸어줌
        jsExecutor.executeScript("var tables = document.querySelectorAll('table');" +
                "tables.forEach(function(table) {" +
                "    table.style.display = 'block';" +
                "});");

        WebElement tables = driver.findElement(By.xpath("/html/body/table"));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<WebElement> rows = tables.findElements(By.tagName("tr"));
        List<WebElement> cells = rows.get(0).findElements(By.tagName("td"));
        List<WebElement> spans = cells.get(0).findElements(By.xpath(".//span[not(.//span)]")); // 중첩 스팬 제거

        int i = 0;
        // 모든 테이블의 졸업 요건 데이터를 저장할 리스트
        String text;
        for (WebElement span : spans) {
            text = span.getText().trim();
            if (text.equals("출력")) {
                i = 1;
                continue;
            }
            if (i == 1 && !span.getText().isEmpty()) { // 값이 비어 있지 않은 경우 출력
                //byte[] utf8Bytes = text.getBytes(UTF_8);
                //String utf8Text = new String(utf8Bytes, UTF_8);
                requirementComponentList.add(text);
            }
        }
    }
}

