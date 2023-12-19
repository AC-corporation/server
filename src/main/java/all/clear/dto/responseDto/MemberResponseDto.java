package all.clear.dto.responseDto;

import all.clear.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private Long memberId;
    private Long appId;
    private String appPassword;

    private String memberName;
    private String university;
    private String major;
    private String mail;
    private String classType; //분반
    private int level; //학년
    private int semester; //학기

    public MemberResponseDto(Member member){
        this.memberId = member.getMemberId();
        this.appId = member.getAppId();
        this.appPassword = member.getAppPassword();
        this.memberName = member.getMemberName();
        this.university = member.getUniversity();
        this.major = member.getMajor();
        this.mail = member.getMail();
        this.classType = member.getClassType();
        this.level = member.getLevel();
        this.semester = member.getSemester();
    }
}