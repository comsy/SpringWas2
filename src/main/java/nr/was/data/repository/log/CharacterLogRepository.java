package nr.was.data.repository.log;

import nr.was.data.domain.log.CharacterLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterLogRepository extends JpaRepository<CharacterLog, Long> {

    List<CharacterLog> findByGuid(Long guid);

    @Override
    <S extends CharacterLog> S save(S entity);

    @Override
    void delete(CharacterLog entity);


}
