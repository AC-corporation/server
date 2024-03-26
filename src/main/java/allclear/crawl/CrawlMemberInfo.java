package allclear.crawl;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import allclear.domain.member.Member;
import allclear.global.exception.GlobalException;
import allclear.global.exception.code.GlobalErrorCode;
import lombok.Getter;

public class CrawlMemberInfo {

    @Getter private Member member;

    WebDriver driver;
    WebElement target; // 크롤링 할 요소

    public CrawlMemberInfo(WebDriver driver) {

        this.driver = driver;

        // 유저 크롤링
        try {
            crawlMemberComponent();
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorCode._USAINT_CRAWLING_FAILED);
        }
    }

    public void crawlMemberComponent() { // 사용자 정보 크롤링 함수

        WebElement managementButton =
                driver.findElement(
                        By.xpath("/html/body/div[2]/div/div[2]/header/div[2]/div[1]/ul/li[3]"));
        managementButton.click(); // 학사관리 버튼 클릭

        try {
            Thread.sleep(2000); // 2초 동안 실행을 멈추기
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 프레임 이동
        WebElement iframe1Element = driver.findElement(By.name("contentAreaFrame"));
        driver.switchTo().frame(iframe1Element);
        WebElement iframe2Element = driver.findElement(By.name("isolatedWorkArea"));
        driver.switchTo().frame(iframe2Element);

        // memberName 크롤링
        try {
            Thread.sleep(4000); // 4초 동안 실행을 멈추기
        } catch (Exception e) {
            e.printStackTrace();
        }

        // user_name 크롤링
        target =
                driver.findElement(
                        By.xpath(
                                "/html/body/table/tbody/tr/td/div/div[1]/span/span[3]/table/tbody[2]/tr/td/span/span/div/table/tbody/tr[1]/td[1]/table/tbody/tr/td/table/tbody/tr[3]/td[2]/span/input"));
        String user_name = target.getAttribute("value");

        // university 크롤링
        target =
                driver.findElement(
                        By.xpath(
                                "/html/body/table/tbody/tr/td/div/div[1]/span/span[3]/table/tbody[2]/tr/td/span/span/div/table/tbody/tr[1]/td[1]/table/tbody/tr/td/table/tbody/tr[1]/td[4]/span/input"));
        String university = target.getAttribute("value");

        // major 크롤링
        target =
                driver.findElement(
                        By.xpath(
                                "/html/body/table/tbody/tr/td/div/div[1]/span/span[3]/table/tbody[2]/tr/td/span/span/div/table/tbody/tr[1]/td[1]/table/tbody/tr/td/table/tbody/tr[2]/td[4]/span/input"));
        String major = target.getAttribute("value");

        // mail 크롤링
        target =
                driver.findElement(
                        By.xpath(
                                "/html/body/table/tbody/tr/td/div/div[1]/span/span[3]/table/tbody[2]/tr/td/span/span/div/table/tbody/tr[1]/td[1]/table/tbody/tr/td/table/tbody/tr[8]/td[2]/span/input"));
        String mail = target.getAttribute("value");

        // classType 크롤링
        target =
                driver.findElement(
                        By.xpath(
                                "/html/body/table/tbody/tr/td/div/div[1]/span/span[3]/table/tbody[2]/tr/td/span/span/div/table/tbody/tr[1]/td[1]/table/tbody/tr/td/table/tbody/tr[4]/td[4]/span/input"));
        String classType = target.getAttribute("value");

        // level(학년) 크롤링
        target =
                driver.findElement(
                        By.xpath(
                                "/html/body/table/tbody/tr/td/div/div[1]/span/span[3]/table/tbody[2]/tr/td/span/span/div/table/tbody/tr[1]/td[1]/table/tbody/tr/td/table/tbody/tr[5]/td/table/tbody/tr/td/table/tbody/tr/td[2]/span/input"));
        int level = Integer.parseInt(target.getAttribute("value").strip());

        // semester(학기) 크롤링
        target =
                driver.findElement(
                        By.xpath(
                                "/html/body/table/tbody/tr/td/div/div[1]/span/span[3]/table/tbody[2]/tr/td/span/span/div/table/tbody/tr[1]/td[1]/table/tbody/tr/td/table/tbody/tr[5]/td/table/tbody/tr/td/table/tbody/tr/td[4]/span/input"));
        int semester = Integer.parseInt(target.getAttribute("value").strip());

        // 입학 연도 크롤링
        target =
                driver.findElement(
                        By.xpath(
                                "/html/body/table/tbody/tr/td/div/div[1]/span/span[3]/table/tbody[2]/tr/td/span/span/div/table/tbody/tr[1]/td[1]/table/tbody/tr/td/table/tbody/tr[1]/td[2]/span/input"));
        String admissionYear = target.getAttribute("value").strip();

        // 전공 크롤링
        target =
                driver.findElement(
                        By.xpath(
                                "/html/body/table/tbody/tr/td/div/div[1]/span/span[3]/table/tbody[2]/tr/td/span/span/div/table/tbody/tr[1]/td[1]/table/tbody/tr/td/table/tbody/tr[3]/td[4]/span/input"));

        String detailMajor;
        if (target == null) detailMajor = null;
        else detailMajor = target.getAttribute("value").strip();

        member =
                Member.builder()
                        .username(user_name)
                        .university(university)
                        .major(major)
                        .email(mail)
                        .classType(classType)
                        .level(level)
                        .semester(semester)
                        .admissionYear(admissionYear)
                        .detailMajor(detailMajor)
                        .build();

        // 기본 프레임으로 돌아가기
        driver.switchTo().defaultContent();

        // 웹 드라이버 종료
        driver.close();
    }
}
