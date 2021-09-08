package nr.server.game.domain.character.api;

import lombok.Getter;
import nr.server.common.api.GenericApi;
import nr.server.domain.db.data.character.data.CharacterDto;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CharacterAddExpApi {

    @Getter
    public static class Request extends GenericApi.Request {
        @NotNull
        private final Long guid;

        @NotNull
        private final Long id;

        @NotNull
        private final int addExp;

        public Request(@NotNull String version, @NotNull String token, Long guid, Long id, int addExp) {
            super(version, token);
            this.guid = guid;
            this.id = id;
            this.addExp = addExp;
        }
    }

    @Getter
    public static class Response extends GenericApi.Response{
        @NotNull
        private final List<CharacterDto> characterList;
        @NotNull
        private final Boolean isLevelUp;

        public Response(List<CharacterDto> characterList, Boolean isLevelUp) {
            super(HttpStatus.OK.value(), "");
            this.characterList = characterList;
            this.isLevelUp = isLevelUp;
        }
    }
}
