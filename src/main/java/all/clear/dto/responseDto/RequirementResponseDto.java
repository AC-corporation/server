package all.clear.dto.responseDto;

import all.clear.domain.requirement.Requirement;
import all.clear.domain.requirement.RequirementComponent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequirementResponseDto {
    private Long requirementId;
    private List<RequirementComponentResponseDto> requirementComponentList = new ArrayList<>();

    public RequirementResponseDto(Requirement requirement) {
        this.requirementId = requirement.getRequirementId();
        List<RequirementComponentResponseDto> requirementComponentResponseDtoList = requirement.getRequirementComponentList()
                .stream()
                .map(RequirementComponentResponseDto::from)
                .collect(Collectors.toList());
        this.requirementComponentList = requirementComponentResponseDtoList;
    }

    public RequirementResponseDto of(Requirement requirement){
        List<RequirementComponentResponseDto> requirementComponentResponseDtoList = requirement.getRequirementComponentList()
                .stream()
                .map(RequirementComponentResponseDto::from)
                .collect(Collectors.toList());

        return RequirementResponseDto.builder()
                .requirementId(requirement.getRequirementId())
                .requirementComponentList(requirementComponentResponseDtoList)
                .build();
    }

}
