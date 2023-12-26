package allclear;

import allclear.crawl.CrawlMemberInfo;
import allclear.domain.grade.Grade;
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
        crawlMemberInfo = new CrawlMemberInfo("20203058",  "Jjw112233@");
        requirement = crawlMemberInfo.getRequirement();
        // grade = crawlMemberInfo.getGrade();

        requirementComponentList = requirement.getRequirementComponentList();
        for(int i=0;i<requirementComponentList.size();i++){
            RequirementComponent requirementComponent = requirementComponentList.get(i);
            System.out.println(requirementComponent.getRequirementCategory());
            System.out.println(requirementComponent.getRequirementArgument());
            System.out.println(requirementComponent.getRequirementCriteria());
            System.out.println(requirementComponent.getRequirementComplete());
            System.out.println(requirementComponent.getRequirementResult());
            System.out.println("================================");
        }
    }
}
