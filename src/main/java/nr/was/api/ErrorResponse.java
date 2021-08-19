package nr.was.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import nr.was.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
public class ErrorResponse extends GenericApi.Response{

    @JsonProperty("errorCode")
    @NotNull
    private final String errorCode;

    @JsonProperty("errors")
    private final List<FieldError> errors;


    private ErrorResponse(final ErrorCode code, final List<FieldError> errors) {
        super(code.getStatus(), code.getMessage());
        this.errorCode = code.name();
        this.errors = errors;
    }

    private ErrorResponse(final ErrorCode code) {
        super(code.getStatus(), code.getMessage());
        this.errorCode = code.name();
        this.errors = new ArrayList<>();
    }

    private ErrorResponse(final ErrorCode code, final String customMessage) {
        super(code.getStatus(), customMessage);
        this.errorCode = code.name();
        this.errors = new ArrayList<>();
    }


    public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
        return new ErrorResponse(code, FieldError.of(bindingResult));
    }

    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    public static ErrorResponse of(final ErrorCode code, final String customMessage) {
        if(customMessage.isEmpty())
            return new ErrorResponse(code);
        else
            return new ErrorResponse(code, customMessage);
    }

    public static ErrorResponse of(final ErrorCode code, final List<FieldError> errors) {
        return new ErrorResponse(code, errors);
    }

    public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
        final String value = e.getValue() == null ? "" : e.getValue().toString();
        final List<FieldError> errors = FieldError.of(e.getName(), value, e.getErrorCode());
        return new ErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

        private FieldError(final String field, final String value, final String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }


}