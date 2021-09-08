package nr.server.domain.db.data.user.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nr.server.domain.db.data.BaseGameTimeCachedEntity;

import javax.persistence.*;

@Entity
@Table(name = "user") // character 는 예약어임
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)      // 빌더 패턴으로만 new 하기 위함.
@AllArgsConstructor(access = AccessLevel.PROTECTED)     // 빌더 패턴으로만 new 하기 위함.
@SuperBuilder
//@DynamicUpdate // 변경한 필드만 대응
public class User extends BaseGameTimeCachedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long guid;

    private int level;

    private int exp;

    @Override
    public String getCacheKey() {
        return guid + "";
    }
}
