package nr.was.api;

import nr.was.data.dto.CharacterDto;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CharacterAddExpApi {

    @Getter
    public static class Request extends GenericApi.Request{
        @NotNull
        private Long guid;

        @NotNull
        private Long id;

        @NotNull
        private int addExp;

        public Request(@NotNull String version, @NotNull String token, Long guid, Long id, int addExp) {
            super(version, token);
            this.guid = guid;
            this.id = id;
            this.addExp = addExp;
        }
    }

    @Getter
    public static class Response extends GenericApi.Response{
        private final List<CharacterDto> characterList;
        private final Boolean isLevelUp;

        public Response(@NotNull int status, @NotNull String message, List<CharacterDto> characterList, Boolean isLevelUp) {
            super(status, message);
            this.characterList = characterList;
            this.isLevelUp = isLevelUp;
        }
    }
}
