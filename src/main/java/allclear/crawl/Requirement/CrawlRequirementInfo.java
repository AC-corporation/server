package allclear.crawl.Requirement;

import allclear.domain.requirement.Requirement;
import allclear.global.exception.GlobalException;
import allclear.global.exception.code.GlobalErrorCode;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.ArrayList;

public class CrawlRequirementInfo {

    @Getter
    private Requirement requirement;

    private ArrayList<String> requirementComponentList = new ArrayList<>();

    WebDriver driver;
    WebElement target; // 크롤링 할 요소
    String targetPath; // 크롤링 할 요소가 있는 경로
    String targetText; // 크롤링 할 문자

    public CrawlRequirementInfo(WebDriver driver) {

        this.driver = driver;

        try {
            crawlRequirementComponent();
        }
        catch (Exception e) {
            throw new GlobalException(GlobalErrorCode._USAINT_CRAWLING_FAILED);
        }

        try {
            requirement = ParsingRequirement.parsingRequirementString(requirementComponentList);
        }
        catch (Exception e) {
            throw new GlobalException(GlobalErrorCode._USAINT_PARSING_FAILED);
        }

    }

    public void crawlRequirementComponent() { // 졸업요건 조회 크롤링 함수

        // 학사 관리 버튼 클릭
        WebElement managementButton = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/header/div[2]/div[1]/ul/li[3]"));
        managementButton.click();
        // 성적/졸업 버튼 클릭
        WebElement gradeAndGraduationButton = driver.findElement(By.xpath("//*[@id=\"8d3da4feb86b681d72f267880ae8cef5\"]"));
        gradeAndGraduationButton.click();

        try {
            Thread.sleep(500); // 0.5초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 졸업사정표 버튼 클릭
        WebElement graduationRequirementButton = driver.findElement(By.xpath("//*[@id=\"30f2303171c98bdf57db799d0b834646\"]/a"));
        graduationRequirementButton.click();

        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 프레임 이동
        WebElement iframe1Element = driver.findElement(By.name("contentAreaFrame"));
        driver.switchTo().frame(iframe1Element);
        WebElement iframe2Element = driver.findElement(By.name("isolatedWorkArea"));
        driver.switchTo().frame(iframe2Element);

        try {
            Thread.sleep(5000); // 5초 동안 실행을 멈추기, 3초보다 작을 경우 오류 발생
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 졸업요건 문자열 리스트에 추가
        boolean exit_flag = false;
        int tr = 2; // 행

        while (true) {

            for (int td = 1; td <= 6; td++) {
                targetPath = "/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[4]/td/" +
                        "table/tbody[2]/tr/td/div/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[" + tr + "]/td[" + td + "]";
                try {
                    target = driver.findElement(By.xpath(targetPath));
                    targetText = target.getText().strip();

                    if (targetText.equals("채플")) // 채플까지 도달하면 종료
                        exit_flag = true;
                } catch (Exception e) {
                    targetText = ""; // 요소가 없는 경우
                    continue;
                }
                requirementComponentList.add(targetText);
            }
            if (exit_flag)
                break;
            tr = tr + 1;
        }

        // 기본 프레임 이동
        driver.switchTo().defaultContent();

        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.close(); // 웹 드라이버 닫기
    }
}
