package all.clear;

import all.clear.crwal.CrwalUserInfo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class crwalTest {
    @Test
    public static void main(String[] args) {
        CrwalUserInfo crwalUserinfo = new CrwalUserInfo();
        crwalUserinfo.loginUsaint("20192396","wjdtntkdtjs13@26");
        System.out.println(crwalUserinfo.getUserName());
        System.out.println(crwalUserinfo.getUniversity());
        System.out.println(crwalUserinfo.getMajor());
        System.out.println(crwalUserinfo.getMail());
        System.out.println(crwalUserinfo.getClassType());
        System.out.println(crwalUserinfo.getYear());
        System.out.println(crwalUserinfo.getSemester());
        ArrayList<String> tmpList = crwalUserinfo.getRequirementComponentList();
        System.out.println("************졸업 요건 리스트****************");
        for (int i=0;i<tmpList.size();i++){
            System.out.println(tmpList.get(i));
        }
        System.out.println("************전체 학기 성적****************");
        crwalUserinfo.crawlEntireGrades();
        ArrayList<String> entires = crwalUserinfo.getEntireGrades();
        for(int i = 0 ; i<entires.size(); i++){
            System.out.println(entires.get(i));
        }
        System.out.println("************학기별 세부 성적***************");
        crwalUserinfo.crawlDetailGrades();
        ArrayList<String> details = crwalUserinfo.getDetailGrades();
        for(int i =0 ; i<details.size(); i++)
        {
            System.out.println(details.get(i));
        }
    }
}
