package nr.was.domain.character.api;

import lombok.Getter;
import nr.was.domain.character.data.CharacterDto;
import nr.was.global.api.GenericApi;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CharacterAddApi {

    @Getter
    public static class Request extends GenericApi.Request {
        @NotNull
        private final Long guid;

        public Request(String version, String token, Long guid) {
            super(version, token);
            this.guid = guid;
        }
    }

    @Getter
    public static class Response extends GenericApi.Response{
        @NotNull
        private final List<CharacterDto> characterList;

        public Response(List<CharacterDto> characterList) {
            super(HttpStatus.OK.value(), "");
            this.characterList = characterList;
        }
    }
}
