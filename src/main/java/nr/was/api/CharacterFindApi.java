package nr.was.api;

import nr.was.data.dto.CharacterDto;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CharacterFindApi {

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
        private final List<CharacterDto> characterList;

        public Response(@NotNull int status, @NotNull String message, List<CharacterDto> characterList) {
            super(status, message);
            this.characterList = characterList;
        }
    }
}
