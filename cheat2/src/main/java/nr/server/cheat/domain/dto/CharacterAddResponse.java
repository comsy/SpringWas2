package nr.server.cheat.domain.dto;

import lombok.Getter;
import nr.server.domain.db.data.character.data.Character;
import nr.server.domain.db.data.character.data.CharacterDto;

import java.util.List;

@Getter
public class CharacterAddResponse {
    private List<CharacterDto> characterDtoList;

    public void setCharacterDtoList(List<CharacterDto> characterDtoList){
        this.characterDtoList = characterDtoList;
    }
}
