package com.adnstyle.jwtapi.common;

import com.adnstyle.jwtapi.domain.GhMember;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class MemberDetail implements UserDetails, Serializable {

    private static final Long serialVersionUID = 1L;

    private GhMember ghMember;


    public MemberDetail(GhMember ghMember) {
        this.ghMember = ghMember;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(ghMember.getRole()));
    }

    @Override
    public String getPassword() {
        return ghMember.getPassword();
    }

    @Override
    public String getUsername() {
        return ghMember.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        if (ghMember.getLockYN().equals("N")) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (ghMember.getLockYN().equals("N")) {
            return true;
        } else {
            return false;
        }
    }


}