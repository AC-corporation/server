
package all.clear.domain;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Subject {
    /**성적 조회, 졸업요건 조회 이후 추후 상의 **/
    @Id @GeneratedValue
    @Column(name = "subject_id")
    private Long subjectId;
    @Column(name = "subject_name")
    private String subjectName; //과목 이름
    private String professor;
    private Long credit; //학점
    @Column(name = "requirement_list")
    private List<String> requirementList; //졸업 요건

    @OneToMany(mappedBy = "subject")
    private List<TimeTableSubject> timeTableSubjectList;
}


