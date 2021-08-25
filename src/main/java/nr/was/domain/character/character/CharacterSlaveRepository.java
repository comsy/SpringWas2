package nr.was.domain.character.character;

import nr.was.global.annotation.RepositorySlave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@RepositorySlave
public interface CharacterSlaveRepository extends JpaRepository<Character, Long> {

    List<Character> findByGuid(Long guid);
}
