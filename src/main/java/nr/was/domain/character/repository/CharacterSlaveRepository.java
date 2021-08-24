package nr.was.domain.character.repository;

import nr.was.domain.character.Character;
import nr.was.global.annotation.RepositorySlave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@RepositorySlave
public interface CharacterSlaveRepository extends JpaRepository<Character, Long> {

    List<Character> findByGuid(Long guid);
}
