package allclear.dto.responseDto;

import allclear.domain.member.Member;
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
    private String email;
    private String password;

    private String memberName;
    private String university;
    private String major;
    private String classType; //분반
    private Integer level; //학년
    private Integer semester; //학기

    public MemberResponseDto(Member member){
        this.memberId = member.getMemberId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.memberName = member.getMemberName();
        this.university = member.getUniversity();
        this.major = member.getMajor();
        this.classType = member.getClassType();
        this.level = member.getLevel();
        this.semester = member.getSemester();
    }
}
