package allclear.global.exception;


import allclear.global.exception.code.GlobalErrorCode;

public class GlobalExceptionHandler extends GlobalException{
    public GlobalExceptionHandler(GlobalErrorCode errorCode) {
        super(errorCode);
    }
}
