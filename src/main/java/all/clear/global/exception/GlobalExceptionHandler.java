package all.clear.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends GlobalException{
    public GlobalExceptionHandler(GlobalErrorCode errorCode) {
        super(errorCode);
    }
}
