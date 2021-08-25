package nr.was.domain.character.charactersub;

import nr.was.global.annotation.RepositoryRedis;
import org.springframework.data.repository.CrudRepository;

@RepositoryRedis
public interface CharacterSubRepository extends CrudRepository<CharacterSub, Long> {

    CharacterSub findByName(String name);
}