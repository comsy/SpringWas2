package nr.was.domain.user.user;

import lombok.*;
import nr.was.global.entity.EntityMaster;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user") // character 는 예약어임
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)      // 빌더 패턴으로만 new 하기 위함.
@AllArgsConstructor(access = AccessLevel.PROTECTED)     // 빌더 패턴으로만 new 하기 위함.
@Builder
//@DynamicUpdate // 변경한 필드만 대응
public class User extends EntityMaster {

    @Id
    @GeneratedValue
    private Long guid;

    private int level;

    private int exp;

    @Override
    public String getCacheKey() {
        return guid + "";
    }
}
