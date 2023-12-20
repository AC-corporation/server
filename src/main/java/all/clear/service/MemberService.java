package all.clear.service;

import all.clear.crawl.CrawlMemberInfo;
import all.clear.domain.Member;
import all.clear.domain.UserDetailsImpl;
import all.clear.domain.grade.Grade;
import all.clear.domain.requirement.Requirement;
import all.clear.dto.requestDto.EmailIsValidRequestDto;
import all.clear.dto.requestDto.LoginRequestDto;
import all.clear.dto.requestDto.MemberSignupRequestDto;
import all.clear.dto.requestDto.UpdateRequestDto;
import all.clear.global.exception.GlobalErrorCode;
import all.clear.global.exception.GlobalException;
import all.clear.repository.GradeRepository;
import all.clear.repository.MemberRepository;
import all.clear.repository.RequirementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;


@Service
@Slf4j
@RequiredArgsConstructor
//@Transactional
public class MemberService {
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final RequirementRepository requirementRepository;
    @Autowired
    private final GradeRepository gradeRepository;
    private final PasswordEncoder passwordEncoder;

    public Member findOne(Long id) {
        return memberRepository.findById(id).get();
    }


    /**
     * 로그인
     */
    public Long login(LoginRequestDto request) {
        String email = request.getEmail();
        String password = request.getPassword();

        Member member = memberRepository.findByEmail(email);

        //비밀번호 확인
        if (!passwordEncoder.matches(password, member.getPassword()))
            throw new GlobalException(GlobalErrorCode._PASSWORD_MISMATCH);
        return member.getMemberId();
    }


    /**
     * 회원가입
     */
    @Transactional
    public void createMember(MemberSignupRequestDto request) throws GlobalException {
        String password = passwordEncoder.encode(request.getPassword());
        Member member = Member.builder()
                .email(request.getEmail())
                .password(password)
                .build();

        String usaintId = request.getUsaintId();
        String usaintPassword = request.getUsaintPassword();

        //멤버 정보 크롤링
        CrawlMemberInfo crawlMemberInfo = new CrawlMemberInfo(usaintId, usaintPassword);

        //크롤링 한 데이터 member에 저장
        Member newMember = crawlMemberInfo.getMember();
        member.setMemberName(newMember.getMemberName());
        member.setUniversity(newMember.getUniversity());
        member.setMajor(newMember.getMajor());
        member.setClassType(newMember.getClassType());
        member.setLevel(newMember.getLevel());
        member.setSemester(newMember.getSemester());

        //졸업요건
        Requirement newRequirement = crawlMemberInfo.getRequirement();
        newRequirement.setMember(member);
        requirementRepository.save(newRequirement);

        //성적
        Grade newGrade = crawlMemberInfo.getGrade();
        newGrade.setMember(member);
        gradeRepository.save(newGrade);

        memberRepository.save(member);
    }


    //회원가입 - 이메일 인증 코드 보내기
    public void sendEmailCode(String email) {

        /**
         * 수정필요
         */

    }


    //회원가입 - 이메일 인증 코드 확인
    public boolean isEmailValid(EmailIsValidRequestDto request) {
        String email = request.getEmail();
        String code = request.getCode();
        /**
         * 수정필요
         */
        return true;
    }


    /**
     * 유저 정보 업데이트
     */
    @Transactional
    public void updateMember(UserDetailsImpl userDetails, UpdateRequestDto updateRequestDto) throws GlobalException {
        Member member = findOne(userDetails.getUser().getMemberId());
        String usaintId = updateRequestDto.getUsaintId();
        String usaintPassword = updateRequestDto.getUsaintPassword();

        CrawlMemberInfo crawlInfo = new CrawlMemberInfo(usaintId, usaintPassword);

        //멤버 초기화
        Member newMember = crawlInfo.getMember();
        member.setMemberName(newMember.getMemberName());
        member.setUniversity(newMember.getUniversity());
        member.setMajor(newMember.getMajor());
        // member.setEmail(newMember.getEmail());
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
