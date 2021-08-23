package nr.was.data.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "characters") // character 는 예약어임
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)      // 빌더 패턴으로만 new 하기 위함.
@AllArgsConstructor(access = AccessLevel.PROTECTED)     // 빌더 패턴으로만 new 하기 위함.
@Builder
//@DynamicUpdate // 변경한 필드만 대응
public class Character extends EntityRoot {

    @Id
    @GeneratedValue
    private Long id;

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
}
