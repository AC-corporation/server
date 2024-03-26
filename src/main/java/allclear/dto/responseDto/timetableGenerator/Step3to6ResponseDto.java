package allclear.dto.responseDto.timetableGenerator;

import java.util.List;
import java.util.stream.Collectors;

import allclear.domain.requirement.RequirementComponent;
import allclear.domain.subject.Subject;
import allclear.dto.responseDto.requirement.RequirementComponentResponseDto;
import allclear.dto.responseDto.subject.SubjectResponseDto;
import lombok.Getter;

@Getter
public class Step3to6ResponseDto {
    private List<RequirementComponentResponseDto> requirementComponentResponseDtoList;
    private List<SubjectResponseDto> subjectResponseDtoList;

    public Step3to6ResponseDto(
            List<RequirementComponent> requirementComponentList, List<Subject> subjectList) {
        this.requirementComponentResponseDtoList =
                requirementComponentList.stream()
                        .map(RequirementComponentResponseDto::from)
                        .collect(Collectors.toList());
        this.subjectResponseDtoList =
                subjectList.stream().map(SubjectResponseDto::new).collect(Collectors.toList());
    }
}
