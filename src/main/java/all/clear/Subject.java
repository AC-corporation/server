
package all.clear;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Subject {
    /**성적 조회, 졸업요건 조회 이후 추후 상의 **/
    @Id @GeneratedValue
    @Column(name = "SUBJECT_ID")
    private Long id;
    @Column(name = "SUBJECT_NAME")
    private String name; //과목 이름
    @Column(name = "PROFESSOR")
    private String professor;
    @Column(name = "CREDIT")
    private Long credit; //학점
    @Column(name = "REQ_LIST")
    private List<String> reqList; //졸업 요건

    @OneToMany(mappedBy = "subject")
    private List<TimeTableSubject> timeTableSubjectList;
}


