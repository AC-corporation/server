package all.clear;

import all.clear.crawl.CrawlInfo;
import org.junit.Test;

import java.util.ArrayList;

public class CrawlSpeedExceptionTest {

    @Test
    public static void main(String[] args){
        CrawlInfo crawlInfo = new CrawlInfo();
        crawlInfo.loginUsaint("20192396","wjdtntkdtjs13@26");
        crawlInfo.crawlRequirementComponent();

        ArrayList<String> tmpList = crawlInfo.getRequirementComponentList();
        System.out.println("************졸업 요건 리스트****************");
        for (int i=0;i<tmpList.size();i++){
            System.out.println(tmpList.get(i));
        }

        System.out.println("************전체 학기 성적****************");
        crawlInfo.crawlEntireGrades();
        ArrayList<String> entires = crawlInfo.getEntireGrades();
        for(int i = 0 ; i<entires.size(); i++){
            System.out.println(entires.get(i));
        }

        System.out.println("************학기별 세부 성적***************");
        crawlInfo.crawlDetailGrades();
        ArrayList<String> details = crawlInfo.getDetailGrades();
        for(int i =0 ; i<details.size(); i++)
        {
            System.out.println(details.get(i));
        }

    }
}
