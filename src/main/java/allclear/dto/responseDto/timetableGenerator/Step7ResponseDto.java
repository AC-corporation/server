package allclear.dto.responseDto.timetableGenerator;

import java.util.List;
import java.util.stream.Collectors;

import allclear.domain.timetableGenerator.TimetableGeneratorSubject;
import allclear.dto.responseDto.timetable.TimetableSubjectResponseDto;
import lombok.Getter;

@Getter
public class Step7ResponseDto {
    List<TimetableSubjectResponseDto> timetableGeneratorSubjectResponseDtoList;
    ;

    public Step7ResponseDto(List<TimetableGeneratorSubject> timetableGeneratorSubjectList) {
        timetableGeneratorSubjectResponseDtoList =
                timetableGeneratorSubjectList.stream()
                        .map(TimetableSubjectResponseDto::new)
                        .collect(Collectors.toList());
    }
}
