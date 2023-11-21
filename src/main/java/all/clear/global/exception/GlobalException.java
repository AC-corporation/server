package all.clear.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GlobalException extends RuntimeException{
    private final GlobalErrorCode errorCode;

    public ErrorResponseDto getErrorReason(){
        return this.errorCode.getReason();
    }

    public ErrorResponseDto getErrorReasonHttpStatus(){
        return this.errorCode.getReasonHttpStatus();
    }
}
