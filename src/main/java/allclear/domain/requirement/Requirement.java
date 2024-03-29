package allclear.domain.requirement;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import allclear.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Requirement {
    @Id
    @GeneratedValue
    @Column(name = "requirement_id")
    private Long requirementId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "requirement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequirementComponent> requirementComponentList;

    // ==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.setRequirement(this);
    }

    public void addRequirementComponent(RequirementComponent requirementComponent) {
        requirementComponent.setRequirement(this);

        if (requirementComponentList == null) {
            requirementComponentList = new ArrayList<>();
            requirementComponentList.add(requirementComponent);
        } else {
            requirementComponentList.add(requirementComponent);
        }
    }
}
