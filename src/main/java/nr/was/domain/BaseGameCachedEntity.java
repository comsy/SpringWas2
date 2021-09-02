package nr.was.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;

@Getter
@MappedSuperclass       // 이 클래스 상속 시 여기 필드도 컬럼으로 인식
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public abstract class BaseGameCachedEntity implements ICachedEntity {


}
