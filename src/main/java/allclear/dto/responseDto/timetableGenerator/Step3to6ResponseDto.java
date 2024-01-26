package allclear.dto.responseDto.timetableGenerator;

import allclear.domain.subject.Subject;
import allclear.dto.responseDto.subject.SubjectResponseDto;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Step3to6ResponseDto {
    private List<SubjectResponseDto> subjectResponseDtoList;

    public Step3to6ResponseDto(List<Subject> subjectList) {
        List<SubjectResponseDto> subjectResponseDtoList = subjectList
                .stream()
                .map(SubjectResponseDto::new)
                .collect(Collectors.toList());
        this.subjectResponseDtoList = subjectResponseDtoList;
    }
}
