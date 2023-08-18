package com.minki.ogani.mapper.user;

import com.minki.ogani.dto.user.UserReqDto;
import com.minki.ogani.dto.user.UserResDto;
import com.minki.ogani.dto.user.UserRoleReqDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    // id 중복체크
    // 리턴값(타입) 메소드 이름(타입 변수명)
    Integer idCheck(String id);

    // email 중복체크
    Integer emailCheck(String email);

    //회원가입
    Integer saveUser(UserReqDto userReqDto);

    // 사용자/관리자 가입 시 권한 추가
    Integer saveRole(UserRoleReqDto userRoleReqDto);

    //조건(이름/전번/비번)에 맞는 아이디 출력
    UserResDto findId(UserReqDto userReqDto);
}
