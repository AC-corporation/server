package allclear.global.exception;

import allclear.global.exception.code.GlobalErrorCode;
import allclear.global.exception.dto.ErrorResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GlobalException extends RuntimeException{
    private GlobalErrorCode errorCode;

    public ErrorResponseDto getErrorReason(){
        return this.errorCode.getReason();
    }

    public ErrorResponseDto getErrorReasonHttpStatus(){
        return this.errorCode.getReasonHttpStatus();
    }
}
