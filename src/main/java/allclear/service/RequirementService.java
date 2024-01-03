package allclear.service;

import allclear.domain.requirement.Requirement;
import allclear.dto.responseDto.requirement.RequirementResponseDto;
import allclear.repository.member.MemberRepository;
import allclear.repository.requirement.RequirementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequirementService {
    private final RequirementRepository requirementRepository;

    private final MemberRepository memberRepository;

    
    public Requirement findByMemberId(Long memberId){
        return memberRepository.findById(memberId).get().getRequirement();
    }

    /**
     * 졸업요건 조회
     */
    public RequirementResponseDto getRequirement(Long memberId){
        Requirement requirement = findByMemberId(memberId);
        return new RequirementResponseDto(requirement);
    }
}
