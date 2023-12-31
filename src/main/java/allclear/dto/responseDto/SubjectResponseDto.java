package allclear.dto.responseDto;

import allclear.domain.subject.Subject;
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
public class SubjectResponseDto {
    private Long subjectId; //과목 번호
    private String name; //과목 이름
    private String professor;
    private List<String> classInfoList; //강의 시간 및 강의실

    private String department; //개설 학과
    private String majorClassification; //이수 구분(주전공)
    private String multiMajorClassification; //이수 구분(다전공)
    private String engineeringCertification; //공학 인증
    private String classType; //분반
    private Integer credit; //학점
    private Double subjectTime; //과목 시간
    private List<String> subjectTargetList; //수강 대상

    public SubjectResponseDto(Subject subject) {
        this.subjectId = subject.getSubjectId();
        this.name = subject.getName();
        this.professor = subject.getProfessor();
        this.classInfoList = new ArrayList<>(subject.getClassInfoList());
        this.department = subject.getDepartment();
        this.majorClassification = subject.getMajorClassification();
        this.multiMajorClassification = subject.getMultiMajorClassification();
        this.engineeringCertification = subject.getEngineeringCertification();
        this.classType = subject.getClassType();
        this.credit = subject.getCredit();
        this.subjectTime = subject.getSubjectTime();
        this.subjectTargetList = new ArrayList<>(subject.getSubjectTargetList());;
    }
}
