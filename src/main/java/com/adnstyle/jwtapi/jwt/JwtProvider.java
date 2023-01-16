package com.adnstyle.jwtapi.jwt;

import com.adnstyle.jwtapi.security.ErrorCode;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    private String secretKey = "adnstyle123";

    private String jwtHeader = "Authorization";


    private long accessTokenValidTime = Duration.ofMinutes(5).toMillis(); // 만료시간 5분
    private long refreshTokenValidTime = Duration.ofDays(7).toMillis(); // 만료시간 7일


    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    /**JWT -access 토큰을 생성하는 메서드*/
    public String createToken (String id, String role) {
        Claims claims = Jwts.claims().setSubject(id);
        claims.put("role", role);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)//토큰에 넣을 정보 삽입
                .setIssuedAt(now) // 현재 날짜 삽입
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))// 만료 날짜
                .signWith(SignatureAlgorithm.HS256, secretKey) //알고리즘과 키
                .compact();
    }


    /**JWT -refresh 토큰을 생성하는 메서드*/
    public String createRefreshToken (String id, String role) {
        Claims claims = Jwts.claims().setSubject(id);
        claims.put("role", role);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)//토큰에 넣을 정보 삽입
                .setIssuedAt(now) // 현재 날짜 삽입
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))// 만료 날짜
                .signWith(SignatureAlgorithm.HS256, secretKey) //알고리즘과 키
                .compact();
    }

    /**JWT 토큰에서 인증 정보 조회*/
    public Authentication getAuthentication(String token) {

        Claims claims = parseClaims(token);

        if (claims.get("role") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기 : role 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("role").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);

    }


    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }


    /** JWT 토큰 유효성 체크*/
    public boolean validateToken(String token, HttpServletRequest request) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }

        catch (SecurityException | MalformedJwtException | IllegalArgumentException exception) {
            log.debug("잘못된 JWT 입니다");
            request.setAttribute("exception", ErrorCode.WRONG_TYPE_TOKEN.getCode());
        }

        catch (ExpiredJwtException exception) {
            log.debug("만료된 JWT 입니다");
            request.setAttribute("exception", ErrorCode.EXPIRED_TOKEN.getCode());
        }

        catch (UnsupportedJwtException exception) {
            log.debug("지원하지 않는 JWT 입니다");
            request.setAttribute("exception", ErrorCode.UNSUPPORTED_TOKEN.getCode());
        }

        return false;
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (SecurityException | MalformedJwtException | IllegalArgumentException exception) {
            log.info("잘못된 JWT 입니다");

        } catch (ExpiredJwtException exception) {
            log.info("만료된 JWT 입니다");
        } catch (UnsupportedJwtException exception) {
            log.info("지원하지 않는 JWT 입니다");
        }

        return false;
    }

    // Request header에서 token 꺼내옴
    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        // 가져온 Authorization Header 가 문자열이고, Bearer 로 시작해야 가져옴
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }

        return null;
    }

    //header에 삽입
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader(jwtHeader, accessToken);
    }


}
