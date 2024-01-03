package allclear.dto.responseDto.timetable;

import allclear.domain.subject.ClassInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ClassInfoDto {
    //교수명
    private String professor;
    //강의 시간
    private String classTime;
    //강의 요일
    private String classDate;
    //강의 장소
    private String classRoom;

    public ClassInfoDto(ClassInfo classInfo){
        this.professor = classInfo.getProfessor();
        this.classTime = classInfo.getClassTime();
        this.classDate = classInfo.getClassDate();
        this.classRoom = classInfo.getClassRoom();
    }
}
