package nr.server.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    //== HTTP ERROR (100-511) ==//
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(),                 "Invalid Input Value"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(),           "Invalid Input Value"),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST.value(),                    "Entity Not Found"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(),     "Server Error"),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST.value(),                  "Invalid Type Value"),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN.value(),                  "Access is Denied"),


    //== BUSINESS ERROR (1000 ~ ) ==//

    // Member
    USER_NOT_EXIST              (1000,  "User is not exist."),
    LOGIN_INPUT_INVALID         (1001,  "Login input is invalid"),

    // CHARACTER
    CHARACTER_NOT_EXIST         (2001,  "Character is not exist."),
    CHARACTER_INVALID           (2002,  "Coupon was already expired"),
    CHARACTER_INFO_NOT_EXIST    (2001,  "Character redis info is not exist."),

    ;

    private final int status;
    private final String message;

    ErrorCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public int getStatus() {
        return status;
    }

    public int getHttpStatus(){
        int httpStatus = getStatus();

        // BUSINESS ERROR 기본은 INTERNAL_SERVER_ERROR 로 취급.
        if(getStatus() >= 1000){
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }

        return httpStatus;
    }
}