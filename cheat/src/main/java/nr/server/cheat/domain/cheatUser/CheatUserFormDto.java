package nr.server.cheat.domain.cheatUser;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.Serializable;

@Getter
@ToString
@NoArgsConstructor
public class CheatUserFormDto implements Serializable {
    private String email;

    private String password;

    private String role;

    public String getCryptPassword(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
