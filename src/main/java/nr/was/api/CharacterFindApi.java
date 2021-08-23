package nr.was.api;

import nr.was.data.dto.CharacterDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;

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
        @NotNull
        private final List<CharacterDto> characterList;

        public Response(List<CharacterDto> characterList) {
            super(HttpStatus.OK.value(), "");
            this.characterList = characterList;
        }
    }
}
