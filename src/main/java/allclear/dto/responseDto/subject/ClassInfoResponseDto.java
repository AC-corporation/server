package allclear.dto.responseDto.subject;

import allclear.domain.subject.ClassInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassInfoResponseDto {
    //교수명
    private String professor;
    //강의 요일
    private String classDay;
    //강의 시작 시간
    private LocalTime startTime;
    //강의 종료 시간
    private LocalTime endTime;
    //강의 장소
    private String classRoom;

    public ClassInfoResponseDto(ClassInfo classInfo){
        this.professor = classInfo.getProfessor();
        this.classDay = classInfo.getClassDay();
        this.startTime = classInfo.getStartTime();
        this.endTime = classInfo.getEndTime();
        this.classRoom = classInfo.getClassRoom();
    }
}
