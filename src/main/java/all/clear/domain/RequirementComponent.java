package all.clear.domain;

import jakarta.persistence.*;

@Entity
public class RequirementComponent {
    @Id @GeneratedValue
    @Column(name = "requirement_component_id")
    private Long requirementComponentId;

    @ManyToOne
    @JoinColumn(name = "requirement_id")
    private Requirement requirement;

    @Column(name = "requirement_category")
    private String requirementCategory; //이수구분

    @Column(name = "requirement_argument")
    private String requirementArgument; //졸업요건

    @Column(name = "requirement_criteria")
    private Double requirementCriteria; //기준값
    @Column(name = "requirement_complete")
    private Double requirementComplete; //계산값

}
