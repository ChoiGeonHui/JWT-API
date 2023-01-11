package com.adnstyle.jwtapi.repository;


import com.adnstyle.jwtapi.domain.GhMember;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GhMemberRepository {

    int selectCount();


//    List<GhMember> selectMemberList();

    GhMember selectMember(GhMember ghMember);

//    int insertMember(GhMember ghMember);
//
//
//    int insertSocialMember(GhMember ghMember);
//
//    int updateMember(GhMember ghMember);
//
//    int updatePassword(GhMember ghMember);
//
//    void failCount(GhMember ghMember);
//
//    int failCountReset(GhMember ghMember);
//
//    void lockMember(GhMember ghMember);
//
//    int unLockMember(GhMember ghMember);

}
