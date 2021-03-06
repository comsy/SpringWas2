package nr.server.domain.db.data.character.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nr.server.domain.db.data.BaseGameTimeCachedEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "characters") // character 는 예약어임
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)      // 빌더 패턴으로만 new 하기 위함.
@AllArgsConstructor(access = AccessLevel.PROTECTED)     // 빌더 패턴으로만 new 하기 위함.
@SuperBuilder
//@DynamicUpdate // 변경한 필드만 대응
public class Character extends BaseGameTimeCachedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(updatable = false, nullable = false)
    private Long guid;

    private int category;

    private Long characterId;

    private int level;

    private int exp;

    @Override
    public String getCacheKey() {
        return guid+":"+id;
    }

    public boolean addExpAndLevelUp(int addExp){
        this.exp += addExp;

        boolean isLevelUp = false;
        if(this.exp > 1200){
            this.level = 4;
            isLevelUp = true;
        }
        else if(this.exp > 900){
            this.level = 3;
            isLevelUp = true;
        }
        else if(this.exp > 600){
            this.level = 2;
            isLevelUp = true;
        }
        else if(this.exp > 300){
            this.level = 1;
            isLevelUp = true;
        }

        return isLevelUp;
    }

    public void changeLevel(){

        this.level++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return category == character.category && level == character.level && exp == character.exp && id.equals(character.id) && guid.equals(character.guid) && Objects.equals(characterId, character.characterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, guid, category, characterId, level, exp);
    }
}
