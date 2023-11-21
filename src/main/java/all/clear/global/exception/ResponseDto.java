package all.clear.global.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder
@AllArgsConstructor
@Getter
public class ResponseDto {
    private  Boolean isSuccess;
    private String code;
    private String message;
    private HttpStatus httpStatus;
}
