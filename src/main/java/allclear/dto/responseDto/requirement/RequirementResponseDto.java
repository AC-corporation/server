package allclear.dto.responseDto.requirement;

import java.util.List;

import allclear.domain.requirement.Requirement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequirementResponseDto {
    private Long requirementId;
    private List<RequirementComponentResponseDto> requirementComponentList;

    public static RequirementResponseDto from(Requirement requirement) {
        List<RequirementComponentResponseDto> requirementComponentResponseDtoList =
                requirement.getRequirementComponentList().stream()
                        .map(RequirementComponentResponseDto::from)
                        .toList();

        return RequirementResponseDto.builder()
                .requirementId(requirement.getRequirementId())
                .requirementComponentList(requirementComponentResponseDtoList)
                .build();
    }
}
