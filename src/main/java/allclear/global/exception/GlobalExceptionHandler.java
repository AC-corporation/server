package allclear.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends GlobalException{
    GlobalErrorCode globalErrorCode;
    public GlobalExceptionHandler(GlobalErrorCode errorCode) {
        super(errorCode);
        this.globalErrorCode = errorCode;
    }
}
