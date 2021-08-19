package nr.was.data.domain.log;

import lombok.*;

import javax.persistence.*;

@Entity
//@Table(name = "tbl_character")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
