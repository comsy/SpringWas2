package nr.was.domain.character.charactersub;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.validation.constraints.NotNull;

@RedisHash("characterInfo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)      // 빌더 패턴으로만 new 하기 위함.
@AllArgsConstructor(access = AccessLevel.PROTECTED)     // 빌더 패턴으로만 new 하기 위함.
@Builder
public class CharacterSub {

    @Id
    @NotNull
    private Long id;

    @NotNull
    private String name;

    public void changeName(String name){
        this.name = name;
    }
}
