package allclear.crawl;

import allclear.domain.grade.Grade;
import allclear.domain.grade.SemesterGrade;
import allclear.domain.grade.SemesterSubject;
import allclear.domain.requirement.Requirement;
import allclear.domain.requirement.RequirementComponent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ParsingTest {
    @Test
    public static void main(String[] args){
        CrawlMemberInfo crawlMemberInfo;
        Requirement requirement;
        Grade grade;

        List<RequirementComponent> requirementComponentList = new ArrayList<>();
        List<SemesterGrade> semesterGradeList = new ArrayList<>();
        List<SemesterSubject> semesterSubjectList = new ArrayList<>();

        crawlMemberInfo = new CrawlMemberInfo("ID",  "PWD");
        requirement = crawlMemberInfo.getRequirement();
        grade = crawlMemberInfo.getGrade();

        requirementComponentList = requirement.getRequirementComponentList();
        semesterGradeList = grade.getSemesterGradeList();
        for(int i=0;i<requirementComponentList.size();i++){
            RequirementComponent requirementComponent = requirementComponentList.get(i);
            System.out.println(requirementComponent.getRequirementCategory());
            System.out.println(requirementComponent.getRequirementArgument());
            System.out.println(requirementComponent.getRequirementCriteria());
            System.out.println(requirementComponent.getRequirementComplete());
            System.out.println(requirementComponent.getRequirementResult());
            System.out.println("================================");
        }
        for(int i=0;i<semesterGradeList.size();i++){
            SemesterGrade semesterGrade = semesterGradeList.get(i);
            semesterSubjectList = semesterGrade.getSemesterSubjectList();
            System.out.println("학기별 평균 성적");
            System.out.println(semesterGrade.getSemesterAverageGrade());
            System.out.println("<====================>");
            for(int j=0;j<semesterSubjectList.size();j++){
                SemesterSubject semesterSubject = semesterSubjectList.get(j);
                System.out.println(semesterSubject.getSemesterSubjectName());
                System.out.println(semesterSubject.getSemesterSubjectScore());
                System.out.println("========================");
            }
        }


    }
}
