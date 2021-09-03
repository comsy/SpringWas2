package nr.was.domain.user.data;

import nr.was.global.annotation.RepositoryMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@RepositoryMaster
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByGuid(Long guid);
}
