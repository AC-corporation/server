package allclear.dto.responseDto.timetable;

import allclear.domain.subject.Subject;
import allclear.domain.timetable.TimetableSubject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimetableSubjectResponseDto {
    private Long timetableSubjectId;
    private Subject subject;
    private String name; //과목 이름
    private String professor;
    private List<String> classInfoList;

    public TimetableSubjectResponseDto(TimetableSubject timetableSubject) {
        this.timetableSubjectId = timetableSubject.getTimetableSubjectId();
        this.subject = timetableSubject.getSubject();
        this.name = timetableSubject.getName();
        this.professor = timetableSubject.getProfessor();
        this.classInfoList = new ArrayList<>(timetableSubject.getClassInfoList());
    }
}
