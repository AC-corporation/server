package allclear.dto.responseDto.requirement;

import allclear.domain.requirement.RequirementComponent;
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

    public static RequirementComponentResponseDto from(RequirementComponent component){
        return RequirementComponentResponseDto.builder()
                .requirementComponentId(component.getRequirementComponentId())
                .requirementCategory(component.getRequirementCategory())
                .requirementArgument(component.getRequirementArgument())
                .requirementCriteria(component.getRequirementCriteria())
                .requirementComplete(component.getRequirementComplete())
                .requirementResult(component.getRequirementResult())
                .build();
    }

}
