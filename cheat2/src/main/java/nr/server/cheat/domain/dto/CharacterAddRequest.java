package nr.server.cheat.domain.dto;

import lombok.Getter;

@Getter
public class CharacterAddRequest {
    private Long guid;
    private Long characterId;

    public void setGuid(Long guid){
        this.guid = guid;
    }

    public void setCharacterId(Long characterId){
        this.characterId = characterId;
    }
}
