package all.clear.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Requirement {
    @Id @GeneratedValue
    @Column(name = "requirement_id")
    private Long requirementId;
    @OneToOne(mappedBy = "requirement", fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "requirement")
    @Column(name = "requirement_component_list")
    private List<RequirementComponent> requirementComponentList;
}


