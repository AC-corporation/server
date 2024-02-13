package allclear.crawl.Grade;

import allclear.domain.grade.Grade;
import allclear.domain.member.Member;
import allclear.global.exception.GlobalException;
import allclear.global.exception.code.GlobalErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CrawlGradeAndCurriculumInfo {

    @Getter
    private Grade grade;
    @Getter
    private Member member;
    @Getter
    private List<Long> curriculumSubjectIdList = new ArrayList<>(); // 전공 교과 과정 리스트
    private ArrayList<String> entireGrades = new ArrayList<>();  //  전체 성적 리스트
    private ArrayList<String> detailGrades = new ArrayList<>(); // 학기별 세부 성적 리스트
    private ArrayList<Long> prevSubjectIdList = new ArrayList<>(); // 이전 수강한 과목 id 리스트
    private String totalCredit; // 총 이수 학점
    private String averageGrade; // 평균 학점
    private String firstYear; // 입학 학년도
    private String firstSemester; // 입학한 학기

    WebDriver driver;
    WebElement target; // 크롤링 할 요소
    String targetPath; // 크롤링 할 요소가 있는 경로
    String targetText; // 크롤링 할 문자

    public CrawlGradeAndCurriculumInfo(WebDriver driver, Member member) {

        this.driver = driver;
        this.member = member;

        try {
            crawlEntireGrades();
            crawlDetailGrades();
            crawlCurriculumSubject(member.getAdmissionYear(), member.getUniversity(), member.getMajor(), member.getDetailMajor());
        }
        catch (Exception e) {
            throw new GlobalException(GlobalErrorCode._USAINT_CRAWLING_FAILED);
        }

        try {
            grade = ParsingGrade.parsingGradeString(totalCredit, averageGrade, entireGrades, detailGrades);
            prevSubjectIdList = (ArrayList<Long>) ParsingGrade.prevSubjectIdList;
            this.member.setPrevSubjectIdList(prevSubjectIdList);
        }
        catch (Exception e) {
            throw new GlobalException(GlobalErrorCode._USAINT_PARSING_FAILED);
        }

    }

    // 전체 성적 조회 함수
    public void crawlEntireGrades() {

        // 반복문 위한 cnt;
        int cnt = 1;

        // 학사 관리 버튼 클릭
        WebElement managementButton = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/header/div[2]/div[1]/ul/li[3]"));
        managementButton.click();
        try {Thread.sleep(100); // 0.1초 동안 실행을 멈추기
        } catch (InterruptedException e) {e.printStackTrace();}
        // 성적/졸업 버튼 클릭
        WebElement gradeAndGraduationButton = driver.findElement(By.xpath("//*[@id=\"8d3da4feb86b681d72f267880ae8cef5\"]"));
        gradeAndGraduationButton.click();
        try {Thread.sleep(100); // 0.1초 동안 실행을 멈추기
        } catch (InterruptedException e) {e.printStackTrace();}
        // 학기별 성적 조회 클릭
        WebElement gradeCheckBtn = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[1]/div[2]/ul/li[1]/a"));
        gradeCheckBtn.click();

        try {
            Thread.sleep(5000); // 5초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // web frame 변경
        WebElement iframe1 = driver.findElement(By.xpath("//*[@id=\"contentAreaFrame\"]"));
        driver.switchTo().frame(iframe1);
        try {Thread.sleep(100); // 0.1초 동안 실행을 멈추기
        } catch (InterruptedException e) {e.printStackTrace();}
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

        // 존재한다면 팝업 창 닫기 클릭
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
                Thread.sleep(4000); // 4초 동안 실행을 멈추기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // web frame 변경
            driver.switchTo().frame(iframe1);
            driver.switchTo().frame(iframe2);
        }
        catch (Exception e) {
            // 기본 frame으로 frame 재설정
            driver.switchTo().defaultContent();

            try {
                Thread.sleep(4000); // 4초 동안 실행을 멈추기
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            // web frame 변경
            driver.switchTo().frame(iframe1);
            driver.switchTo().frame(iframe2);
        }

        boolean endFlag = false;

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
                }
                catch (Exception e) // 요소가 더 이상 없을 때 까지 반복하기 때문에 예외처리 해줌
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

        // 크롤링 환경 셋팅 시작
        // 기본 프레임 이동
        driver.switchTo().defaultContent();

        try {
            Thread.sleep(5000); // 5초 동안 실행을 멈추기
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
            boolean inEndFlag = false;
            boolean yearFlag = false; // 학년도 충족 플래그
            boolean semesterFlag = false; // 학기 충족 플래그

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
                yearFlag = true;
            if (firstSemester.equals(selectedSemester))
                semesterFlag = true;

            // 학기별 세부 성적 크롤링
            while (true) {
                for (int i = 2; i <= 9; i++) {
                    if (i == 5 || i == 8) //  크롤링 안 되는 열 예외 처리
                        continue;
                    try {
                        targetPath = "/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[12]/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[1]/table/tbody/tr[" + cnt + "]/td[" + i + "]/span/span";
                        target = driver.findElement(By.xpath(targetPath));
                        targetText = target.getText().strip();
                    }
                    catch (Exception e) {
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
            if (yearFlag && semesterFlag)
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
    public void crawlCurriculumSubject(String admissionYear, String university, String major, String detailMajor) {
        Actions scroll = new Actions(driver); // 콤보박스 스크롤하기 위한 요소

        try {
            Thread.sleep(500); // 0.5초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        target = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/header/div[2]/div[1]/ul/li[3]"));
        target.click(); // 학사 관리 클릭
        target = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/header/div[2]/div[1]/ul/li[3]/div/ul/li[2]/a"));
        target.click(); // 수강신청/교과과정 클릭
        target = driver.findElement(By.xpath("/html/body/div[2]/div/div[2]/div[1]/div[2]/ul/li[9]/a"));
        target.click(); // 입학연도 별 교과과정 클릭

        try {
            Thread.sleep(500); // 0.5초 동안 실행을 멈추기
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
            Thread.sleep(500); // 0.5초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 입학 년도 설정
        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr[1]/td/table/tbody/tr/td/table/tbody/tr[1]/td/div/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[1]/input"));
        target.click();
        while (true) {
            if (target.getAttribute("value").substring(0, 4).equals(admissionYear)) {
                scroll.click().perform();
                break;
            }
            scroll.sendKeys(Keys.ARROW_UP).perform();
        }

        try {
            Thread.sleep(1000); // 1초 동안 실행을 멈추기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 단과 대학 설정
        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr[1]/td/table/tbody/tr/td/table/tbody/tr[2]/td[1]/div/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[1]/input"));
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
        target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr[1]/td/table/tbody/tr/td/table/tbody/tr[2]/td[2]/div/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[1]/input"));
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
            target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr[1]/td/table/tbody/tr/td/table/tbody/tr[2]/td[3]/div/table/tbody/tr/td[2]/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[1]/input"));
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
                if (exitFlag) // 예외가 2번 연속적으로 발생할 경우 종료
                    break;

                try {
                    Thread.sleep(500); // 0.5초 동안 실행을 멈추기
                } catch (InterruptedException interruptedException) {
                }

                target = driver.findElement(By.xpath("/html/body/table/tbody/tr/td/div/table/tbody/tr/td/table/tbody/tr[2]/td/div/table/tbody/tr[3]/td/table/tbody/tr/td/table/tbody/tr[2]/td/div/table/tbody/tr/td/table/tbody[1]/tr[2]/td[1]/div/div[2]/table/tbody[1]"));
                target.click(); // 스크롤 클릭
                scroll.sendKeys(Keys.PAGE_DOWN).perform(); // 스크롤 다운
                exitFlag = true;

                try {
                    Thread.sleep(500); // 0.5초 동안 실행을 멈추기
                } catch (InterruptedException interruptedException) {
                }

                tr--; // 예외로 인해 크롤링 하지 못한 부분 다시 재시도
            }
            tr++;
        }
        driver.quit();
    }

}
