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
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)


        ;

        http
                .formLogin()
                .usernameParameter("id").passwordParameter("password")
                .loginPage("/member/login")
                .loginProcessingUrl("/member/sginIn") //해당 주소로 오는 로그인을 가로채서 요청 처리
                .failureHandler(loginFailHandler)
                .successHandler(loginSuccessHandler)

                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .accessDeniedPage("/access/no-role")


                .and()
                .logout()
                .logoutSuccessUrl("/member/login")

                .and()
                .exceptionHandling()
                .accessDeniedPage("/access/no-role");

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
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
