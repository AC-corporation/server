package allclear.dto.requestDto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ChangePasswordDto {
    String userId; // 유저가 사용 하는 아이디
    String currentPassword; // 현재 비밀 번호
    String newPassword; // 바꾸고 싶은 비밀 번호
}
