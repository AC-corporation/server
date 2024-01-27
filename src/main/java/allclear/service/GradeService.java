package allclear.service;

import allclear.domain.grade.Grade;
import allclear.domain.grade.SemesterGrade;
import allclear.domain.member.Member;
import allclear.dto.responseDto.grade.GradeResponseDto;
import allclear.dto.responseDto.grade.SemesterGradeResponseDto;
import allclear.global.exception.GlobalException;
import allclear.global.exception.code.GlobalErrorCode;
import allclear.repository.grade.GradeRepository;
import allclear.repository.grade.SemesterGradeRepository;
import allclear.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GradeService {
    private final GradeRepository gradeRepository;

    private final MemberRepository memberRepository;

    private final SemesterGradeRepository semesterGradeRepository;


    public Grade findByMemberId(Long memberId){
        Member targetMember = memberRepository.findById(memberId).orElseThrow
                (() -> new GlobalException(GlobalErrorCode._NO_CONTENTS));
        return targetMember.getGrade();
    }

    /**
     * 전체 성적 조회
     */
    public GradeResponseDto getGrade(Long memberId){
        Grade grade = findByMemberId(memberId);
        if (grade == null)
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS);
        return new GradeResponseDto(grade);
    }

    /**
     * 학기별 성적 조회
     */
    public SemesterGradeResponseDto getSemesterGradeResponse(Long semesterGradeId){
        SemesterGrade semesterGrade = semesterGradeRepository.findById(semesterGradeId).orElse(null);
        if (semesterGrade == null)
            throw new GlobalException(GlobalErrorCode._NO_CONTENTS);
        return new SemesterGradeResponseDto(semesterGrade);
    }
}
