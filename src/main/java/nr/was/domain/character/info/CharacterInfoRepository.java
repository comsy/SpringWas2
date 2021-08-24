package nr.was.domain.character.info;

import nr.was.global.annotation.RepositoryRedis;
import org.springframework.data.repository.CrudRepository;

@RepositoryRedis
public interface CharacterInfoRepository extends CrudRepository<CharacterInfo, Long> {

    CharacterInfo findByName(String name);
}