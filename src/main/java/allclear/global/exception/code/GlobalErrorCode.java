package allclear.global.exception.code;

import allclear.global.exception.dto.ErrorResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Getter
@AllArgsConstructor
//@Component
//@Slf4j
public enum GlobalErrorCode {
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    //회원가입 이메일 인증 코드
    _INVALID_CODE(HttpStatus.UNAUTHORIZED, "4001", "유효하지 않은 코드입니다."),
    _PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "4002", "비밀번호가 일치하지 않습니다."),
    _ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "4004", "존재하지 않는 계정입니다."),
    _DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "4005", "이미 등록된 이메일입니다"),

    //유세인트
    _USAINT_LOGIN_FAILED(HttpStatus.BAD_REQUEST, "4101", "유세인트 아이디 또는 비밀번호가 잘못되었습니다."),
    _USAINT_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "4102", "유세인트 서버를 이용할 수 없습니다."),
    _USAINT_CRAWLING_FAILED(HttpStatus.NOT_IMPLEMENTED, "4103", "크롤링에 실패했습니다.")
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
