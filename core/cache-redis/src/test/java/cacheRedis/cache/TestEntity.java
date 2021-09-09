package cacheRedis.cache;

import lombok.*;
import nr.server.core.cacheRedis.base.ICachedEntity;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)      // 빌더 패턴으로만 new 하기 위함.
@AllArgsConstructor(access = AccessLevel.PROTECTED)     // 빌더 패턴으로만 new 하기 위함.
@Builder
//@DynamicUpdate // 변경한 필드만 대응
public class TestEntity implements ICachedEntity {

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
}
