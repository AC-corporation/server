package allclear.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Getter
@AllArgsConstructor
@Component
public enum GlobalErrorCode {
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    //회원가입 이메일 인증 코드
    _INVALID_CODE(HttpStatus.UNAUTHORIZED, "4001", "유효하지 않은 코드입니다"),
    _PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "4002", "비밀번호가 일치하지 않습니다"),
    _ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "4004", "존재하지 않는 계정입니다")
        ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    public ErrorResponseDto getReason(){
        return ErrorResponseDto.builder()
                .code(code)
                .message(message)
                .isSuccess(false)
                .build();
    }

    public ErrorResponseDto getReasonHttpStatus(){
        return ErrorResponseDto.builder()
                .code(code)
                .message(message)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }

}
