package all.clear;

import jakarta.persistence.*;

@Entity
public class Requirement {
    @Id @GeneratedValue
    @Column(name = "REQ_ID")
    private Long id;
    @OneToOne(mappedBy = "requirement") // 추가
    private User user;
}


