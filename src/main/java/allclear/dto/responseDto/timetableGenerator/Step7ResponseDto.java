package allclear.dto.responseDto.timetableGenerator;

import allclear.domain.subject.Subject;
import allclear.domain.timetableGenerator.TimetableGeneratorSubject;
import allclear.dto.responseDto.timetable.TimetableSubjectResponseDto;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Step7ResponseDto {
    List<TimetableSubjectResponseDto> timetableSubjectResponseDtoList;

    public Step7ResponseDto(List<TimetableGeneratorSubject> timetableGeneratorSubjectList) {
        timetableSubjectResponseDtoList = timetableGeneratorSubjectList
                .stream()
                .map(TimetableSubjectResponseDto::new)
                .collect(Collectors.toList());
    }
}
