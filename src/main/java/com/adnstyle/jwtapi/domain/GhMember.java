package com.adnstyle.jwtapi.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Alias("ghMember")
@Data
@NoArgsConstructor
public class GhMember implements Serializable {

    /** 식별자 */
    private Long seq;

    /** 아이디 */
    private String id;

    /** 비밀번호 */
    private String password;

    /** 이름 */
    private String name;

    /** 이메일 */
    private String email;

    /** 휴대폰 번호 */
    private String phone;

    /** 사용자 권한 */
    private String role;

    /** 소셜 플랫폼명 */
    private String provider;

    /** 소셜 플랫폼 아이디 */
    private String providerId;

    /** 탈퇴 여부 */
    private String delYN;

    /** 생성일자 */
    private Date createdMember;

    /** 제재 여부 */
    private String lockYN;

    /** 로그인 실패 횟수 */
    private int failCount;


}

