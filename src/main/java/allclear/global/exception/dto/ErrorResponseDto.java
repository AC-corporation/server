package allclear.global.exception.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ErrorResponseDto {
    private Boolean isSuccess;
    private String code;
    private String message;
    private HttpStatus httpStatus;
}
