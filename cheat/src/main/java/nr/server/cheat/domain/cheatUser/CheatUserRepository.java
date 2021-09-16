package nr.server.cheat.domain.cheatUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface CheatUserRepository extends JpaRepository<CheatUser, Long> {

    Optional<CheatUser> findByEmail(String email);
}
