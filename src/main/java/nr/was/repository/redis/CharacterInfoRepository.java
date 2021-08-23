package nr.was.repository.redis;

import nr.was.data.domain.redis.CharacterInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterInfoRepository extends CrudRepository<CharacterInfo, Long> {

    CharacterInfo findByName(String name);
}