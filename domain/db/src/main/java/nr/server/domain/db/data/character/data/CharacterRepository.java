package nr.server.domain.db.data.character.data;

import nr.server.domain.db.annotation.RepositoryMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@RepositoryMaster
public interface CharacterRepository extends JpaRepository<Character, Long> {

    List<Character> findByGuid(Long guid);
}
