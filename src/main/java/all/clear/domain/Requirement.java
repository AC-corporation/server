package all.clear.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Requirement {
    @Id @GeneratedValue
    @Column(name = "requirement_id")
    private Long requirementId;
    @OneToOne(mappedBy = "requirement", fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "requirement")
    @Column(name = "requirement_component_list")
    private List<RequirementComponent> requirementComponentList;


    //==연관관계 메서드==//
    public void setUser(User user){
        this.user = user;
        user.setRequirement(this);
    }

    public void addRequirementComponent(RequirementComponent requirementComponent){
        requirementComponentList.add(requirementComponent);
        requirementComponent.setRequirement(this);
    }
}


