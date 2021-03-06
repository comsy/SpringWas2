package nr.server.domain.db.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import nr.server.core.cacheRedis.base.ICachedEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass       // 이 클래스 상속 시 여기 필드도 컬럼으로 인식
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public abstract class BaseGameTimeCachedEntity implements ICachedEntity {

    // Entity가 생성되어 저장될 때 시간이 자동 저장됩니다.
    @CreatedDate
    private LocalDateTime createdDate;

    // 조회한 Entity 값을 변경할 때 시간이 자동 저장됩니다.
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
