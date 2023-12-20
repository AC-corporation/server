package all.clear.service;

import all.clear.domain.grade.Grade;
import all.clear.dto.responseDto.grade.GradeResponseDto;
import all.clear.repository.GradeRepository;
import all.clear.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GradeService {
    private final GradeRepository gradeRepository;

    private final MemberRepository memberRepository;


    public Grade findByMemberId(Long memberId){
        return memberRepository.findById(memberId).get().getGrade();
    }

    /**
     * 성적 조회
     */
    public GradeResponseDto getGrade(Long memberId){
        Grade grade = findByMemberId(memberId);
        return new GradeResponseDto(grade);
    }
}
