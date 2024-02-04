package allclear.domain.subject;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subject {
    @Id
    @Column(name = "subject_id")
    private Long subjectId; //과목 번호
    @Column(name = "subject_name")
    private String subjectName; //과목 이름
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    @Column(name = "class_info_list")
    private List<ClassInfo> classInfoList = new ArrayList<>(); //강의 시간, 요일, 강의실, 교수명


    private String department; //개설 학과
    @Column(name = "major_classification")
    private String majorClassification; //이수 구분(주전공)
    @Column(name = "multi_major_classification")
    private String multiMajorClassification; //이수 구분(다전공)
    @Column(name = "engineering_certification")
    private String engineeringCertification; //공학 인증
    @Column(name = "class_type")
    private String classType; //분반
    private Double credit; //학점
    private Integer design; //설계
    @Column(name = "subject_time")
    private Double subjectTime; //과목 시간
    @Column(name = "subject_target")
    private String subjectTarget; //수강 대상


    public void updateSubject(String subjectName,String majorClassification, String multiMajorClassification, String engineeringCertification,
                              String classType, Double credit, Integer design , Double subjectTime, String subjectTarget)
    {
        this.subjectName = subjectName;
        this.majorClassification = majorClassification;
        this.multiMajorClassification = multiMajorClassification;
        this.engineeringCertification = engineeringCertification;
        this.classType = classType;
        this.credit = credit;
        this.design = design;
        this.subjectTime = subjectTime;
        this.subjectTarget=subjectTarget;
    }

    public void setClassInfoList(List<ClassInfo> classInfoList){
        this.classInfoList=classInfoList;
    }
}
