package all.clear.service;

import all.clear.crwal.CrawlMemberInfo;
import all.clear.domain.Member;
import all.clear.domain.grade.Grade;
import all.clear.domain.requirement.Requirement;
import all.clear.dto.responseDto.MemberResponseDto;
import all.clear.repository.GradeRepository;
import all.clear.repository.MemberRepository;
import all.clear.repository.RequirementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//@Transactional
public class MemberService {
    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final RequirementRepository requirementRepository;

    @Autowired
    private final GradeRepository gradeRepository;

    public Member findOne(Long id) {
        return memberRepository.findById(id).get();
    }

    /**
     * 유저 조회
     */
    public MemberResponseDto getMember(Long id){
        Member member = findOne(id);
        return new MemberResponseDto(member);
    }

    /**
     * 유저 정보 업데이트
     */
    // 크롤링 실패했을 때 처리 추가 필요
    // 유저 정보 없을 때 처리 추가 필요
    public void update(Long id, String usaintId, String usaintPasswd){
        Member member = findOne(id);
        CrawlMemberInfo crawlInfo = new CrawlMemberInfo(usaintId, usaintPasswd);

        //멤버 초기화
        Member newMember = crawlInfo.getMember();
        member.setMemberName(newMember.getMemberName());
        member.setUniversity(newMember.getUniversity());
        member.setMajor(newMember.getMajor());
        member.setMail(newMember.getMail());
        member.setClassType(newMember.getClassType());
        member.setLevel(newMember.getLevel());
        member.setSemester(newMember.getSemester());

        //졸업요건 초기화
        requirementRepository.delete(member.getRequirement());
        Requirement newRequirement = crawlInfo.getRequirement();
        newRequirement.setMember(member);
        requirementRepository.save(newRequirement);

        //성적 초기화
        gradeRepository.delete(member.getGrade());
        Grade newGrade = crawlInfo.getGrade();
        newGrade.setMember(member);
        gradeRepository.save(newGrade);
    }
}
