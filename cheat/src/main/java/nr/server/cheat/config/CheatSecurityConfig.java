package nr.server.cheat.config;

import lombok.RequiredArgsConstructor;
import nr.server.cheat.domain.cheatUser.CheatUserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class CheatSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CheatUserService cheatUserService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        // static 하위 폴더 (css, js, img)는 무조건 접근이 가능해야하기 때문에 인증을 무시
        web.ignoring().antMatchers("/css/**", "/bootstrap/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests() // 6
                .antMatchers("/login", "/register", "/loginProc", "/registerProc", "/user").permitAll() // 누구나 접근 허용
                .antMatchers("/").hasRole("USER") // USER, ADMIN만 접근 가능
                .antMatchers("/admin").hasRole("ADMIN") // ADMIN만 접근 가능
                //.anyRequest().permitAll()       // 나머지 요청들은 접근 가능
                .anyRequest().authenticated()   // 나머지 요청들은 권한의 종류에 상관 없이 권한이 있어야 접근 가능
                .and()
                .formLogin()
                .loginPage("/login") // 로그인 페이지 링크
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/") // 로그인 성공 후 리다이렉트 주소
                .and()
                .logout() // 8
                .logoutSuccessUrl("/login") // 로그아웃 성공시 리다이렉트 주소
                .invalidateHttpSession(true) // 세션 날리기
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(cheatUserService)
                // 해당 서비스(userService)에서는 UserDetailsService를 implements해서
                // loadUserByUsername() 구현해야함 (서비스 참고)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}
