package nr.was.domain.character.character;

import lombok.*;

import javax.persistence.*;

@Entity
//@Table(name = "tbl_character")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)      // 빌더 패턴으로만 new 하기 위함.
@AllArgsConstructor(access = AccessLevel.PROTECTED)     // 빌더 패턴으로만 new 하기 위함.
@Builder
//@DynamicUpdate // 변경한 필드만 대응
public class CharacterLog {

    @Id
    @GeneratedValue
    private Long id;

    private Long guid;

    private int category;

    private Long characterId;
}
