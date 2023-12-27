package allclear.crawl;

import allclear.crawl.CrawlMemberInfo;
import allclear.domain.member.Member;
import allclear.domain.requirement.Requirement;
import allclear.domain.requirement.RequirementComponent;
import allclear.dto.responseDto.requirement.RequirementResponseDto;
import allclear.repository.RequirementRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class RequirementTest {
    @Autowired
    RequirementRepository requirementRepository;

    @Test
    void getRequirement(){
        CrawlMemberInfo crawlMemberinfo = new CrawlMemberInfo("아이디", "비밀번호");

        //로그인하고 임시로 member, requirement 생성
        Member member = new Member();
        Requirement requirement = new Requirement();
        member.setRequirement(requirement);
        requirement.setMember(member);

        //requirement save
        requirementRepository.save(requirement);

        RequirementComponent requirementComponent = new RequirementComponent();


        //requirement와 연관관계 생성
        requirement.addRequirementComponent(requirementComponent);

        //requirement를 dto로 반환
        //원래는 requirementService의 getRequirement(id) 사용
        RequirementResponseDto requirementResponseDto = new RequirementResponseDto(requirement);

        System.out.println("requiremnetResponseDto : " + requirementResponseDto.getRequirementId());
        for (int i = 0; i < requirementResponseDto.getRequirementComponentList().size(); i++) {
            System.out.println("---requirementResponseComponentDto---");
            System.out.println(requirementResponseDto.getRequirementComponentList().get(i).getRequirementCategory());
            System.out.println(requirementResponseDto.getRequirementComponentList().get(i).getRequirementArgument());
            System.out.println(requirementResponseDto.getRequirementComponentList().get(i).getRequirementCriteria());
            System.out.println(requirementResponseDto.getRequirementComponentList().get(i).getRequirementComplete());
            System.out.println(requirementResponseDto.getRequirementComponentList().get(i).getRequirementResult());
        }

    }
}
