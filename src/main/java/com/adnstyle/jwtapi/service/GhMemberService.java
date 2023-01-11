package com.adnstyle.jwtapi.service;


import com.adnstyle.jwtapi.common.MemberDetail;
import com.adnstyle.jwtapi.domain.GhMember;
import com.adnstyle.jwtapi.domain.Role;
import com.adnstyle.jwtapi.repository.GhMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GhMemberService implements UserDetailsService {//UserDetailsService : spring Security 사용시 필수로 상속받아야하는 인터 페이스

    private final GhMemberRepository ghMemberRepository;

    public int selectCount() {
        return ghMemberRepository.selectCount();

    }

    public GhMember selectMember (GhMember ghMember) {
        return ghMemberRepository.selectMember(ghMember);
    }

    /**
     * 사용자의 아이디를 찾아서 암호화된 패스워드랑 매칭 확인및 인증하는 메서드
     * @param id
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        GhMember ghMember = new GhMember();
        ghMember.setId(id);
        ghMember = ghMemberRepository.selectMember(ghMember);

        if (ghMember == null) {
            throw new UsernameNotFoundException("User not authorizied");
        }
        return new MemberDetail (ghMember);
    }


//    /**
//     * 계정 제재
//     * @param ghMember
//     */
//    @Transactional
//    public void lockMember (GhMember ghMember) {
//        ghMemberRepository.lockMember(ghMember);
//    }
//
//
//    /**
//     * 계정 제재 해제
//     * @param ghMember
//     */
//    @Transactional
//    public String unLockMember (GhMember ghMember) {
//        if (ghMemberRepository.unLockMember(ghMember) > 0) {
//            return "success";
//        } else {
//            return "fail";
//        }
//    }

}

