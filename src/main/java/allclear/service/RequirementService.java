package allclear.service;

import org.springframework.stereotype.Service;

import allclear.domain.member.Member;
import allclear.domain.requirement.Requirement;
import allclear.dto.responseDto.requirement.RequirementResponseDto;
import allclear.global.exception.GlobalException;
import allclear.global.exception.code.GlobalErrorCode;
import allclear.repository.member.MemberRepository;
import allclear.repository.requirement.RequirementRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RequirementService {
    private final RequirementRepository requirementRepository;

    private final MemberRepository memberRepository;

    public Requirement findByMemberId(Long memberId) {
        Member targetMember =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(() -> new GlobalException(GlobalErrorCode._NO_CONTENTS));
        return targetMember.getRequirement();
    }

    // 졸업요건 조회
    public RequirementResponseDto getRequirement(Long memberId) {
        Requirement requirement = findByMemberId(memberId);
        if (requirement == null) throw new GlobalException(GlobalErrorCode._NO_CONTENTS);
        return RequirementResponseDto.from(requirement);
    }
}
