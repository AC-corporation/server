package all.clear;

import all.clear.crawl.CrawlMemberInfo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class CrawalTest {
    @Test
    public static void main(String[] args) {
        CrawlMemberInfo crawlMemberInfo = new CrawlMemberInfo();
        crawlMemberInfo.loginUsaint("20203058","비밀번호");
        System.out.println(crawlMemberInfo.getMemberName());
        System.out.println(crawlMemberInfo.getUniversity());
        System.out.println(crawlMemberInfo.getMajor());
        System.out.println(crawlMemberInfo.getMail());
        System.out.println(crawlMemberInfo.getClassType());
        System.out.println(crawlMemberInfo.getYear());
        System.out.println(crawlMemberInfo.getSemester());
        ArrayList<String> tmpList = crawlMemberInfo.getRequirementComponentList();
        System.out.println("************졸업 요건 리스트****************");
        for (int i=0;i<tmpList.size();i++){
            System.out.println(tmpList.get(i));
        }
        System.out.println("************전체 학기 성적****************");
        crawlMemberInfo.crawlEntireGrades();
        ArrayList<String> entires = crawlMemberInfo.getEntireGrades();
        for(int i = 0 ; i<entires.size(); i++){
            System.out.println(entires.get(i));
        }
        System.out.println("************학기별 세부 성적***************");
        crawlMemberInfo.crawlDetailGrades();
        ArrayList<String> details = crawlMemberInfo.getDetailGrades();
        for(int i =0 ; i<details.size(); i++)
        {
            System.out.println(details.get(i));
        }
    }
}
