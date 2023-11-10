package all.clear.domain;

import jakarta.persistence.*;

@Entity
public class Requirement {
    @Id @GeneratedValue
    @Column(name = "requirement_id")
    private Long requirementId;
    @OneToOne(mappedBy = "requirement", fetch = FetchType.LAZY)
    private User user;
}


