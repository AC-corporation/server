package all.clear;

import all.clear.crwal.CrwalUserInfo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class crwalTest {
    @Test
    public static void main(String[] args) {
        CrwalUserInfo crwalUserinfo = new CrwalUserInfo();
        crwalUserinfo.loginUsaint("20223168","todo!9844");
        System.out.println(crwalUserinfo.getUserName());
        System.out.println(crwalUserinfo.getUniversity());
        System.out.println(crwalUserinfo.getMajor());
        System.out.println(crwalUserinfo.getMail());
        System.out.println(crwalUserinfo.getClassType());
        System.out.println(crwalUserinfo.getYear());
        System.out.println(crwalUserinfo.getSemester());
        ArrayList<String> tmpList = crwalUserinfo.getRequirementComponentList();
        System.out.println("==============getRequirementComponentList===============");
        for (int i=0;i<tmpList.size();i++){
            System.out.println(tmpList.get(i));
        }
        System.out.println("==============getEntireGrades===============");
        tmpList = crwalUserinfo.getEntireGrades();
        for (int i=0;i<tmpList.size();i++){
            System.out.println(tmpList.get(i));
        }
        System.out.println("==============getDetailGrades===============");
        tmpList = crwalUserinfo.getDetailGrades();
        for (int i=0;i<tmpList.size();i++){
            System.out.println(tmpList.get(i));
        }
    }
}
