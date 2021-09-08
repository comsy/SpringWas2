package nr.server.domain.db.data.character.data;

import nr.server.domain.db.annotation.RepositorySlave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@RepositorySlave
public interface CharacterSlaveRepository extends JpaRepository<Character, Long> {

    List<Character> findByGuid(Long guid);
}
