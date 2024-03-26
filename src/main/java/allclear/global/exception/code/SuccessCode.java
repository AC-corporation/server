package allclear.global.exception.code;

import org.springframework.http.HttpStatus;

import allclear.global.exception.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    _OK(HttpStatus.OK, "OK", "OK");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public ResponseDto getReason() {
        return ResponseDto.builder().code(code).message(message).isSuccess(true).build();
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
