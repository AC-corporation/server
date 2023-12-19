package all.clear.crwal;

import all.clear.domain.requirement.Requirement;
import all.clear.domain.requirement.RequirementComponent;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class ParsingRequirement {
    /**
     * 졸업요건 문자열 파싱함수
     */
     public static Requirement parsingRequirementString(ArrayList<String> requirementStringList){
        Requirement requirement = new Requirement();
        String requirementString;
        String category = "";
        for(int i=0;i<requirementStringList.size();i++){
            requirementString = requirementStringList.get(i);
            if (requirementString.equals("졸업필수 요건"))
                category = "졸업필수 요건";
            else if (requirementString.equals("교양필수"))
                category = "교양필수";
            else if(requirementString.equals("교양선택"))
                category = "교양선택";
            else if (requirementString.equals("전공기초"))
                category = "전공기초";
            else if (requirementString.equals("전공"))
                category = "전공";
            else if (requirementString.equals("채플"))
                category = "채플";
            else {
                RequirementComponent requirementComponent = new RequirementComponent();
                requirementComponent.setRequirementCategory(category); // 이수구분
                requirementComponent.setRequirementArgument(requirementStringList.get(i)); // 졸업요건
                requirementComponent.setRequirementCriteria(Double.parseDouble(requirementStringList.get(i+1))); // 기준값
                requirementComponent.setRequirementComplete(Double.parseDouble(requirementStringList.get(i+2))); // 계산값
                requirementComponent.setRequirementResult(requirementStringList.get(i+4)); // 충족여부
                requirement.addRequirementComponent(requirementComponent); // 졸업요건 행들을 모으기
                assert requirement != null;
                i = i +4;
            }
        }
        return requirement;
    }
}
