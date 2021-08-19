package nr.was.api;

import nr.was.data.dto.UserDto;
import lombok.*;

import javax.validation.constraints.NotNull;

public class UserFindApi {

    @Getter
    public static class Request extends GenericApi.Request{
        @NotNull
        private Long guid;

        public Request(String version, String token, Long guid) {
            super(version, token);
            this.guid = guid;
        }
    }

    @Getter
    public static class Response extends GenericApi.Response{
        private final UserDto user;

        public Response(@NotNull int status, @NotNull String message, UserDto user) {
            super(status, message);
            this.user = user;
        }
    }
}
