package nr.server.domain.db.data.user.data;

import nr.server.domain.db.annotation.RepositoryMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@RepositoryMaster
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByGuid(Long guid);
}
