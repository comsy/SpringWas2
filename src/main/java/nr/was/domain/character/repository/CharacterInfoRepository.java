package nr.was.domain.character.repository;

import nr.was.domain.character.CharacterInfo;
import nr.was.global.annotation.RepositoryRedis;
import org.springframework.data.repository.CrudRepository;

@RepositoryRedis
public interface CharacterInfoRepository extends CrudRepository<CharacterInfo, Long> {

    CharacterInfo findByName(String name);
}