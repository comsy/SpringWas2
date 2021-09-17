package nr.server.cheat.domain.dto;

import lombok.Getter;

@Getter
public class UserDTO {
    private Long guid;

    public void setGuid(Long guid){
        this.guid = guid;
    }
}
