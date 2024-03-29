package allclear.dto.responseDto.subject;

import java.util.List;
import java.util.stream.Collectors;

import allclear.domain.subject.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectResponseDto {
    private Long subjectId; // 과목 번호
    private String subjectName; // 과목 이름
    private String department; // 개설 학과
    private String majorClassification; // 이수 구분(주전공)
    private String multiMajorClassification; // 이수 구분(다전공)
    private String liberalArtsClassification; // 교과 영역(교양 구분)
    private String engineeringCertification; // 공학 인증
    private String classType; // 분반
    private Double credit; // 학점
    private Integer design; // 설계
    private Double subjectTime; // 과목 시간
    private String subjectTarget; // 수강 대상
    private List<ClassInfoResponseDto> classInfoResponseDtoList; // 강의 시간, 요일, 강의실, 교수명

    public SubjectResponseDto(Subject subject) {
        this.subjectId = subject.getSubjectId();
        this.subjectName = subject.getSubjectName();
        this.classInfoResponseDtoList =
                subject.getClassInfoList().stream()
                        .map(ClassInfoResponseDto::new)
                        .collect(Collectors.toList());
        this.department = subject.getDepartment();
        this.majorClassification = subject.getMajorClassification();
        this.multiMajorClassification = subject.getMultiMajorClassification();
        this.engineeringCertification = subject.getEngineeringCertification();
        this.liberalArtsClassification = subject.getLiberalArtsClassification();
        this.classType = subject.getClassType();
        this.credit = subject.getCredit();
        this.design = subject.getDesign();
        this.subjectTime = subject.getSubjectTime();
        this.subjectTarget = subject.getSubjectTarget();
    }
}
