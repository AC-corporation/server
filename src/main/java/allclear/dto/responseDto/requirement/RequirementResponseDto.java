package allclear.dto.responseDto.requirement;

import allclear.domain.requirement.Requirement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequirementResponseDto {
    private Long requirementId;
    private List<RequirementComponentResponseDto> requirementComponentList;

    public RequirementResponseDto(Requirement requirement) {
        this.requirementId = requirement.getRequirementId();
        List<RequirementComponentResponseDto> requirementComponentResponseDtoList = requirement.getRequirementComponentList()
                .stream()
                .map(RequirementComponentResponseDto::new)
                .collect(Collectors.toList());
        this.requirementComponentList = requirementComponentResponseDtoList;
    }


}
