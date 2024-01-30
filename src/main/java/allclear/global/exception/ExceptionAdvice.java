package allclear.global.exception;

import allclear.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { GlobalException.class })
    protected ApiResponse<String> handleCustomException(GlobalException e) {
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        return ApiResponse.onFailure(e.getErrorCode());
    }

}
