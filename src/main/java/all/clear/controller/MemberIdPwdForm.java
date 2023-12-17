package all.clear.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class MemberIdPwdForm {
    @NotEmpty(message = "유세인트 아이디와 비밀번호는 필수입니다")
    private Long usaintId;
    private String usaintPassword;
    private boolean updateFlag;
}
