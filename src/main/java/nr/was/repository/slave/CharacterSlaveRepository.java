package nr.was.repository.slave;

import nr.was.data.domain.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterSlaveRepository extends JpaRepository<Character, Long> {

    List<Character> findByGuid(Long guid);
}