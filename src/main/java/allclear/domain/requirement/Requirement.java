package allclear.domain.requirement;

import allclear.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Requirement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "requirement_id")
    private Long requirementId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "requirement", cascade = CascadeType.ALL)
    @Column(name = "requirement_component_list")
    private List<RequirementComponent> requirementComponentList = new ArrayList<>();


    //==연관관계 메서드==//
    public void setMember(Member member){
        this.member = member;
        member.setRequirement(this);
    }

    public void addRequirementComponent(RequirementComponent requirementComponent){
        requirementComponentList.add(requirementComponent);
        requirementComponent.setRequirement(this);
    }
}


