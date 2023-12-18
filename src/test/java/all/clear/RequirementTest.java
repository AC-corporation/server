package all.clear;

import all.clear.crwal.CrawlMemberInfo;
import all.clear.domain.Member;
import all.clear.domain.requirement.Requirement;
import all.clear.domain.requirement.RequirementComponent;
import all.clear.dto.responseDto.RequirementResponseDto;
import all.clear.repository.RequirementRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class RequirementTest {
    @Autowired
    RequirementRepository requirementRepository;

    @Test
    void getRequirement(){
        CrawlMemberInfo crawlMemberinfo = new CrawlMemberInfo();
        crawlMemberinfo.loginUsaint("20221826","jk03daeun!");

        //로그인하고 임시로 member, requirement 생성
        Member member = Member.builder().build();
        Requirement requirement = new Requirement();
        member.setRequirement(requirement);
        requirement.setMember(member);

        //requirement save
        requirementRepository.save(requirement);

        //login해서 가져온 졸업요건 요소 리스트
        ArrayList<String> tmpList = crawlMemberinfo.getRequirementComponentList();
        //객체 생성
         RequirementComponent requirementComponent = new RequirementComponent();

         //맨 윗 줄만 테스트
        //요소 직접 set -> 수정 필요
            requirementComponent.setRequirement(requirement);
            requirementComponent.setRequirementCategory(tmpList.get(0));
            requirementComponent.setRequirementArgument(tmpList.get(1));
            requirementComponent.setRequirementCriteria(Double.valueOf(tmpList.get(2)));
            requirementComponent.setRequirementComplete(Double.valueOf(tmpList.get(3)));
            requirementComponent.setRequirementResult(tmpList.get(5));

            //requirement와 연관관계 생성
        requirement.addRequirementComponent(requirementComponent);

        //requirement를 dto로 반환
        //원래는 requirementService의 getRequirement(id) 사용
        RequirementResponseDto requirementResponseDto = new RequirementResponseDto(requirement);

       System.out.println("requiremnetResponseDto : "+requirementResponseDto.getRequirementId());
       for(int i=0; i<requirementResponseDto.getRequirementComponentList().size(); i++){
           System.out.println("---requirementResponseComponentDto---");
           System.out.println(requirementResponseDto.getRequirementComponentList().get(i).getRequirementCategory());
           System.out.println(requirementResponseDto.getRequirementComponentList().get(i).getRequirementArgument());
           System.out.println(requirementResponseDto.getRequirementComponentList().get(i).getRequirementCriteria());
           System.out.println(requirementResponseDto.getRequirementComponentList().get(i).getRequirementComplete());
           System.out.println(requirementResponseDto.getRequirementComponentList().get(i).getRequirementResult());
       }

    }
}
