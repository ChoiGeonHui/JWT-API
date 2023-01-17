package com.adnstyle.jwtapi.security;

import com.adnstyle.jwtapi.domain.Role;
import com.adnstyle.jwtapi.jwt.JwtFilter;
import com.adnstyle.jwtapi.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginFailHandler loginFailHandler;

    private final LoginSuccessHandler loginSuccessHandler;

    private final JwtProvider jwtProvider;

    @Bean
    public BCryptPasswordEncoder encoder() {return new BCryptPasswordEncoder();}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic().disable();

        http
                .authorizeRequests()
//                .antMatchers("/oauth/transform", "oauth/transformMember").hasRole(Role.SOCIAL.name())
                .antMatchers("/main/**").hasAnyRole(Role.USER.name(),Role.ADMIN.name())
                .antMatchers("/error",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.jsp", "/static/**","/valid/**","/member/**","/access/**","/api/**").permitAll()
//                .antMatchers("/board/list/**","/board/view/detail").authenticated()
                .antMatchers("/admin/**").hasRole(Role.ADMIN.name())
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) //인증과정에서 인증실패 또는 인증헤더를 못 받았을 경우 401 에러 대신 실행되는 클래스
                .and()
                .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class) // 서비스 요청마다 실행되는 필터
        ;

        http
                .formLogin()
                .usernameParameter("id").passwordParameter("password")
                .loginPage("/member/login")
                .loginProcessingUrl("/member/sginIn") //해당 주소로 오는 로그인을 가로채서 요청 처리
                .failureHandler(loginFailHandler) // 로그인 실패시 이동
                .successHandler(loginSuccessHandler) // 로그인 성공시 이동

                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .accessDeniedPage("/access/no-role") //권한이 없는 서비스 요청시


                .and()
                .logout()
                .logoutSuccessUrl("/member/login")

                .and()
                .exceptionHandling()
                .accessDeniedPage("/access/no-role");

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 세션 사용하지 않음
        http
                .headers()
                .frameOptions().sameOrigin();

        return http.build();
    }


//    @Bean //security 적용을 하지 않을 url 작성
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> {
//            web.ignoring()
//                    .antMatchers(
//                            "/static/**", "/favicon.ico");
//        };
//    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
