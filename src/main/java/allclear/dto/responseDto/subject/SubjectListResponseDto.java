package allclear.dto.responseDto.subject;

import allclear.domain.subject.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class SubjectListResponseDto {
    List<SubjectResponseDto> subjectResponseDtoList;

    public SubjectListResponseDto(List<Subject> subjectList) {
        List<SubjectResponseDto> subjectResponseDtoList = subjectList
                .stream()
                .map(SubjectResponseDto::new)
                .collect(Collectors.toList());
        this.subjectResponseDtoList = subjectResponseDtoList;
    }
}
