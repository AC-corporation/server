package all.clear.service;

import all.clear.domain.requirement.Requirement;
import all.clear.dto.responseDto.RequirementResponseDto;
import all.clear.repository.MemberRepository;
import all.clear.repository.RequirementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequirementService {
    private final RequirementRepository requirementRepository;

    private final MemberRepository memberRepository;

    public void saveRequirement(Requirement requirement){
        requirementRepository.save(requirement);
    }

    public void deleteRequirement(Requirement requirement){
        requirementRepository.delete(requirement);
    }

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
