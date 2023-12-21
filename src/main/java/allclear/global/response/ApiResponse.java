package allclear.global.response;

import allclear.global.exception.GlobalErrorCode;
import allclear.global.exception.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private final Boolean isSuccess;
    private final String code;
    private final String message;
    private final T data;


    //성공시 응답
    public static <T>ApiResponse<T> onSuccess(String message, T result){
        return new ApiResponse<>(true, SuccessCode._OK.getCode(), message, result);
    }

    public static <T> ApiResponse<T> of(SuccessCode code, T result){
        return new ApiResponse<>(true, code.getReasonHttpStatus().getCode(), code.getReasonHttpStatus().getMessage(), result);
    }


    //실패시 응답
    public static <T>ApiResponse<T> onFailure(GlobalErrorCode code, T result){
        return new ApiResponse<>(false, code.getCode(), code.getMessage() ,result);
    }

}
