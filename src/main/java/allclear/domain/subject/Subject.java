package allclear.domain.subject;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Subject {
    @Id
    @Column(name = "subject_id")
    private Long subjectId; //과목 번호
    private String name; //과목 이름
    private String professor;
    @Column(name = "class_info_list")
    private List<String> classInfoList = new ArrayList<>(); //강의 시간 및 강의실


    private String department; //개설 학과
    @Column(name = "major_classification")
    private String majorClassification; //이수 구분(주전공)
    @Column(name = "multi_major_classification")
    private String multiMajorClassification; //이수 구분(다전공)
    @Column(name = "engineering_certification")
    private String engineeringCertification; //공학 인증
    @Column(name = "class_type")
    private String classType; //분반
    private Integer credit; //학점
    @Column(name = "subject_time")
    private Double subjectTime; //과목 시간
    @Column(name = "subject_target_list")
    private List<String> subjectTargetList = new ArrayList<>(); //수강 대상
}
