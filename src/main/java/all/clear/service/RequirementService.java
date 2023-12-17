package all.clear.service;

import all.clear.domain.Member;
import all.clear.domain.requirement.Requirement;
import all.clear.dto.responseDto.RequirementResponseDto;
import all.clear.repository.RequirementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequirementService {
    private final RequirementRepository requirementRepository;
    private final MemberService memberService;

    public void saveRequirement(Requirement requirement){
        requirementRepository.save(requirement);
    }
    public Requirement findOne(Long id){return requirementRepository.findOne(id);}

    public RequirementResponseDto getRequirement(Long userId){
        Member member = memberService.findOne(userId);
        Requirement requirement = requirementRepository.findByMemberId(userId);
        //userId를 통해 requirementRepo에서 찾은 Requirement를 Dto로 감싸서 반환
        return new RequirementResponseDto(requirement);
    }
}
