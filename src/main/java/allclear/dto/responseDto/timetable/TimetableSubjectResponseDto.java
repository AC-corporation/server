package allclear.dto.responseDto.timetable;

import allclear.domain.timetable.TimetableSubject;
import allclear.dto.responseDto.subject.ClassInfoResponseDto;
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
public class TimetableSubjectResponseDto {
    private Long timetableSubjectId;
    private Long subjectId;
    private String subjectName; //과목 이름
    private String professor;
    private List<ClassInfoResponseDto> classInfoResponseDtoList;

    public TimetableSubjectResponseDto(TimetableSubject timetableSubject) {
        this.timetableSubjectId = timetableSubject.getTimetableSubjectId();
        if (timetableSubject.getSubject() != null)
            this.subjectId = timetableSubject.getSubject().getSubjectId();
        else
            this.subjectId = null;
        this.subjectName = timetableSubject.getSubjectName();
        this.classInfoResponseDtoList = new ArrayList<>(timetableSubject.getClassInfoList()
                .stream()
                .map(ClassInfoResponseDto::new)
                .collect(Collectors.toList())
        );
    }
}
