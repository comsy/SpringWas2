package nr.server.cheat.domain.cheatUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheatUserService implements UserDetailsService {
    private final CheatUserRepository cheatUserRepository;


    /**
     * Spring Security 필수 메소드 구현
     *
     * @param email 이메일
     * @return UserDetails
     * @throws UsernameNotFoundException 유저가 없을 때 예외 발생
     */
    @Override
    public CheatUser loadUserByUsername(String email) throws UsernameNotFoundException {
        return cheatUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }


    public Long register(CheatUserFormDto cheatUserFormDto){
        log.info("form : " +cheatUserFormDto.toString());
        CheatUser cheatUser = CheatUser.builder()
                .email(cheatUserFormDto.getEmail())
                .password(cheatUserFormDto.getCryptPassword())
                .role("ROLE_USER")
                .build();

        CheatUser savedCheatUser = cheatUserRepository.save(cheatUser);

        return savedCheatUser.getId();
    }

    public Long login(CheatUserFormDto cheatUserFormDto){
        log.debug(cheatUserFormDto.toString());
        CheatUser cheatUser = cheatUserRepository.findByEmail(cheatUserFormDto.getEmail()).orElseThrow(() -> new UsernameNotFoundException(cheatUserFormDto.getEmail()));

        return 1L;
    }
}
