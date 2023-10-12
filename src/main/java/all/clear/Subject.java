
package all.clear;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.List;

@Entity
public class Subject {
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
}


