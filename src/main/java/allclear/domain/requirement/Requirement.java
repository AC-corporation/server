package allclear.domain.requirement;

import allclear.domain.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Requirement {
    @Id @GeneratedValue
    @Column(name = "requirement_id")
    private Long requirementId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "requirement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @Column(name = "requirement_component_list")
    private List<RequirementComponent> requirementComponentList;


    //==연관관계 메서드==//
    public void setMember(Member member){
        this.member = member;
        member.setRequirement(this);
    }

    public void addRequirementComponent(RequirementComponent requirementComponent){
        requirementComponent.setRequirement(this);
        this.requirementComponentList.add(requirementComponent);
    }
}


