// 스프링시큐리티를 관리하는 클래스
package com.minki.ogani.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.sql.DataSource;

@Configuration //설정파일을 만들기 위한 애노테이션 or Bean을 등록하기 위한 애노테이션(골뱅이)이다
@EnableWebSecurity //스프링 시큐리티 활성화 해줌 >> 로그인/로그아웃 기능을 할 수 있게 해준다.
public class SecurityConfig {
    @Autowired //DataSource라는 클래스를 밑에서 dataSource 라는 변수로 사용하려고 Autowired에 연결시킨것
    private DataSource dataSource;


    /* 로그인 실패 핸들러 의존성 주입 */
    @Autowired  //AuthenticationFailureHandler 요 클래스를 사용하기 위해 연결시켜준것 >> 로그인 실패했을때 실패문구 나오게 하는 것
    private AuthenticationFailureHandler customFailureHandler;


    @Bean //Beandmf 통해 바로 아래 메소드를 설정파일에 등록하는 것
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 권한에 따라 허용하는 url 설정
        // /login, /signup 페이지는 모두 허용, 다른 페이지는 인증된 사용자만 허용

        // url에 접근 권한을 주는 부분 >> 즉, 권한에 따라 허용하는 url 설정
        http
                .authorizeHttpRequests(
                        (authorizeHttpRequests) ->
                                authorizeHttpRequests
                                        .requestMatchers("/", "/static/**","/user/**" ).permitAll() // 이 경로들은 로그인 없이 접근 가능
                                        .requestMatchers("/admin/**").hasRole("ADMIN") // admin 관련 페이지는 admin 권한을 가진자에게만 접근 허용 하겠다.
                                        .anyRequest().authenticated() // 나머지 url은 로그인해야지만 접근 가능!
                );

        // login 설정(로그인 api 필요 없음) >> 로그인 버튼 누르면 api 대신 실행
        http
                .formLogin((formLogin) ->
                        formLogin
                                .loginPage("/user/login") //로그인 페이지 api url 연결
                                .loginProcessingUrl("/user/loginProc") // 프론트에서 로그인 버튼 클릭 시 연결하는 url!
                                .usernameParameter("id") // id 값 받는 역할
                                .passwordParameter("password") // pw 값 받는 역할
                                .defaultSuccessUrl("/", true) // 로그인 성공시 가는 페이지
                                .failureHandler(customFailureHandler) // 로그인 실패 핸들러 >> 콘피규레이션에서 customFailureHandler을 의미
                                .permitAll() //접근 허용
                );

        // logout 설정(로그아웃 api 필요 없음) >> 로그아웃 버튼 누르면 api 대신 실행
        http
                .logout((logout) ->
                        logout
                                .logoutSuccessUrl("/") //로그아웃 성공하면 "/"(보통 "/index"를 의미) 경로의 api로 가라!
                                .invalidateHttpSession(true).deleteCookies("JSESSIONID") // 로그아웃 시 로그인 정보를 없앤다.
                                .permitAll() // "/" >> 여기 경로로 로그인 안해도 접근 가능하다!
                );

        http.sessionManagement((x)->
                        x.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

        return http.build(); // 권한 허용 로그인 로그아웃 >> 이 3가지 설정을 저장한다.
    }

    // 비밀번호 암호화
    // passwordEncoder라는 이름으로 비번 암호화 메소드 만들기,  passwordEncoder() 호출 시  BCryptPasswordEncoder 이게 리턴되면서 암호화 가능
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authentication 로그인 , Authorization 권한
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
       // 아래 2줄이 로그인 시 아이디/비번에 관한 정보를 체크하기 위해 디비에 접근하는 코드
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder()) //비번 암호화 및 테이블에 있는 비번이랑 맞는지 체크
                // 인증처리 >> 로그인 시 아디 비번 맞는지 확인
                .usersByUsernameQuery("select id, password, true "
                        + "from user "
                        + "where id = ?")
                // 권한처리 >> 정보 일치 시 admin or user인지에 따라 권한 부여
                .authoritiesByUsernameQuery("select m.id, r.name "
                        + "from user_role ur inner join user m on ur.user_id=m.user_id "
                        + "inner join role r on ur.role_id=r.role_id "
                        + "where m.id = ?");
    }


}

