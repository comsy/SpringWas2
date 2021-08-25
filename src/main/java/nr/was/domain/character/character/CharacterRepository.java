package nr.was.domain.character.character;

import nr.was.global.annotation.RepositoryMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@RepositoryMaster
public interface CharacterRepository extends JpaRepository<Character, Long> {

    List<Character> findByGuid(Long guid);
}
