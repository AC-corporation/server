package allclear;

import allclear.crawl.CrawlMemberInfo;
import allclear.domain.member.Member;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class CrawalTest {
    @Test
    public static void main(String[] args) {
        CrawlMemberInfo crawlMemberInfo = new CrawlMemberInfo("ID", "PWD");
        Member member = crawlMemberInfo.getMember();
        System.out.println(member.getMemberName());
        System.out.println(member.getUniversity());
        System.out.println(member.getMajor());
        System.out.println(member.getEmail());
        System.out.println(member.getClassType());
        System.out.println(member.getLevel());
        System.out.println(member.getSemester());


        ArrayList<String> tmpList = crawlMemberInfo.getRequirementComponentList();
        System.out.println("************졸업 요건 리스트****************");
        for (int i=0;i<tmpList.size();i++){
            System.out.println(tmpList.get(i));
        }
        /*
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

        */
    }
}
