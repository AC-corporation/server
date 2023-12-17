package all.clear;

import all.clear.crwal.CrwalMemberInfo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class CrwalTest {
    @Test
    public static void main(String[] args) {
        CrwalMemberInfo crwalMemberInfo = new CrwalMemberInfo();
        crwalMemberInfo.loginUsaint("20203058","비밀번호");
        System.out.println(crwalMemberInfo.getMemberName());
        System.out.println(crwalMemberInfo.getUniversity());
        System.out.println(crwalMemberInfo.getMajor());
        System.out.println(crwalMemberInfo.getMail());
        System.out.println(crwalMemberInfo.getClassType());
        System.out.println(crwalMemberInfo.getYear());
        System.out.println(crwalMemberInfo.getSemester());
        ArrayList<String> tmpList = crwalMemberInfo.getRequirementComponentList();
        System.out.println("************졸업 요건 리스트****************");
        for (int i=0;i<tmpList.size();i++){
            System.out.println(tmpList.get(i));
        }
        System.out.println("************전체 학기 성적****************");
        crwalMemberInfo.crawlEntireGrades();
        ArrayList<String> entires = crwalMemberInfo.getEntireGrades();
        for(int i = 0 ; i<entires.size(); i++){
            System.out.println(entires.get(i));
        }
        System.out.println("************학기별 세부 성적***************");
        crwalMemberInfo.crawlDetailGrades();
        ArrayList<String> details = crwalMemberInfo.getDetailGrades();
        for(int i =0 ; i<details.size(); i++)
        {
            System.out.println(details.get(i));
        }
    }
}
