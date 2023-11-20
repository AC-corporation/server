package all.clear.dto.responseDto;

import all.clear.domain.requirement.RequirementComponent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequirementComponentResponseDto {
    private Long requirementComponentId;
    private String requirementCategory; //이수구분
    private String requirementArgument; //졸업요건
    private Double requirementCriteria; //기준값
    private Double requirementComplete; //계산값
    private String requirementResult; //중족여부

    public RequirementComponentResponseDto(RequirementComponent requirementComponent) {
        this.requirementComponentId = requirementComponent.getRequirementComponentId();
        this.requirementCategory = requirementComponent.getRequirementCategory();
        this.requirementArgument = requirementComponent.getRequirementArgument();
        this.requirementCriteria = requirementComponent.getRequirementCriteria();
        this.requirementComplete = requirementComponent.getRequirementComplete();
        this.requirementResult = requirementComponent.getRequirementResult();
    }



}
