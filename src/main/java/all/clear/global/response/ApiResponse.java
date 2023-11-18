package all.clear.global.response;

import all.clear.global.exception.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private final Boolean isSuccess;
    private final String code;
    private final String message;
    private final T data;


    //성공시 응답
    public static <T>ApiResponse<T> onSuccess(T result){
        return new ApiResponse<>(true, SuccessCode._OK.getCode(), SuccessCode._OK.getMessage(), result);
    }

//    public static <T> ApiResponse<T> of(BaseCode code, T result){
//        return new ApiResponse<>();
//    }


    //실패시 응답
    public static <T>ApiResponse<T> onFailure(String code, String message, T result){
        return new ApiResponse<>(false, code, message ,result);
    }

}
