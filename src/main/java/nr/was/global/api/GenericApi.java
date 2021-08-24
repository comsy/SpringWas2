package nr.was.global.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

public abstract class GenericApi {

    @Getter
    @AllArgsConstructor
    public abstract static class Request {

        @NotNull
        private final String version;

        @NotNull
        private final String token;
    }

    @Getter
    @AllArgsConstructor
    public abstract static class Response {
        @JsonProperty("status")
        @NotNull
        private final int status;

        @JsonProperty("message")
        @NotNull
        private final String message;
    }
}
