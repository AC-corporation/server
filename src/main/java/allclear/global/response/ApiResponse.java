package allclear.global.response;

import allclear.global.exception.code.GlobalErrorCode;
import allclear.global.exception.code.SuccessCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "data"})
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String code;
    private final String message;
    private final T data;


    //성공시 응답
    public static <T>ApiResponse<T> onSuccess(String message, T result){
        return new ApiResponse<>(true, SuccessCode._OK.getCode(), message, result);
    }

    public static ApiResponse onSuccess(String message){
        return new ApiResponse<>(true, SuccessCode._OK.getCode(), message, null);
    }


    //실패시 응답
    public static <T>ApiResponse<T> onFailure(GlobalErrorCode code, T result){
        return new ApiResponse<>(false, code.getCode(), code.getMessage() ,result);
    }

    public static ApiResponse onFailure(GlobalErrorCode code){
        return new ApiResponse<>(false, code.getCode(), code.getMessage() , null);
    }

}
