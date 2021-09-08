package nr.server.domain.redis.data.character;

import nr.server.domain.redis.annotation.RepositoryRedis;
import org.springframework.data.repository.CrudRepository;

@RepositoryRedis
public interface CharacterSubRepository extends CrudRepository<CharacterSub, Long> {

    CharacterSub findByName(String name);
}