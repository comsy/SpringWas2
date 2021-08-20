package nr.was.data.domain;

import lombok.*;

import javax.persistence.*;

@Entity
//@Table(name = "tbl_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)      // 빌더 패턴으로만 new 하기 위함.
@AllArgsConstructor(access = AccessLevel.PROTECTED)     // 빌더 패턴으로만 new 하기 위함.
@Builder
public class User implements CachedEntityInterface {

    @Id
    @GeneratedValue
    @Column(name = "guid")
    private Long guid;

    @Column(nullable = false)
    private Short pmLevel;

    @Column(length = 45)
    private String pmName;


    @Override
    public String getCacheKey() {
        return guid + "";
    }
}
