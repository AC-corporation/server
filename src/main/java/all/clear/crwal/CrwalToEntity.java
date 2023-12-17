package all.clear.crwal;

import all.clear.domain.Member;
import all.clear.domain.requirement.Requirement;
import all.clear.domain.requirement.RequirementComponent;
import all.clear.service.RequirementService;
import all.clear.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
@Component
@Transactional
@Getter
@RequiredArgsConstructor
public class CrwalToEntity {
    private final MemberService memberService;
    private final RequirementService requirementService;


    public void makeUserEntity(CrwalUserInfo crwalUserInfo){
        Long userId;
        Member member = new Member();
        member.setUserName(crwalUserInfo.getUserName()); // 이름
        member.setUniversity(crwalUserInfo.getUniversity()); // 단과대
        member.setMail(crwalUserInfo.getMail()); // 메일
        member.setMajor(crwalUserInfo.getMajor()); // 전공
        member.setClassType(crwalUserInfo.getClassType()); // 분반
        member.setLevel(crwalUserInfo.getYear()); // 학년
        member.setSemester(crwalUserInfo.getSemester()); // 학기
        // userId memberService.join(member);
    }
    public void makeRequirementComponentEntity(CrwalUserInfo crwalUserInfo){
        Requirement requirement = null;
        ArrayList<String> tmpList=  crwalUserInfo.getRequirementComponentList(); // 크롤링해온 졸업요건 문자열을 리스트로 반환
        String tmp;
        String category = "";
        for(int i=0;i<tmpList.size();i++){
            tmp = tmpList.get(i);
            if (tmp.equals("졸업필수 요건"))
                category = "졸업필수 요건";
            else if (tmp.equals("교양필수"))
                category = "교양필수";
            else if(tmp.equals("교양선택"))
                category = "교양선택";
            else if (tmp.equals("전공기초"))
                category = "전공기초";
            else if (tmp.equals("전공"))
                category = "전공";
            else if (tmp.equals("채플"))
                category = "채플";
            else {
                RequirementComponent requirementComponent = new RequirementComponent();
                requirementComponent.setRequirementCategory(category); // 이수구분
                requirementComponent.setRequirementArgument(tmpList.get(i)); // 졸업요건
                requirementComponent.setRequirementCriteria(Double.parseDouble(tmpList.get(i+1))); // 기준값
                requirementComponent.setRequirementComplete(Double.parseDouble(tmpList.get(i+2))); // 계산값
                requirementComponent.setRequirementResult(tmpList.get(i+4)); // 충족여부
                requirement.addRequirementComponent(requirementComponent); // 졸업요건 행들을 모으기
                assert requirement != null;
                i = i +4;
            }
        }
        requirementService.saveRequirement(requirement);
    }
}
