package allclear.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    _OK(HttpStatus.OK, "OK", "OK")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public ResponseDto getReason() {
        return ResponseDto.builder()
                .code(code)
                .message(message)
                .isSuccess(true)
                .build();
    }


    public ResponseDto getReasonHttpStatus() {
        return ResponseDto.builder()
                .code(code)
                .message(message)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}
