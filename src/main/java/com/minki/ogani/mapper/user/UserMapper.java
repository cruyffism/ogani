package com.minki.ogani.mapper.user;

import com.minki.ogani.dto.user.UserReqDto;
import com.minki.ogani.dto.user.UserResDto;
import com.minki.ogani.dto.user.UserRoleReqDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    // id 중복체크
    // 리턴값(타입) 메소드(xml이랑연결하기 위해서 쓴다! xml 아이디와 같아야함) 이름(타입 변수명)
    // select 제외하고는 전부 리턴값(타입)은 기본적으로 Integer
    Integer idCheck(String id);

    // email 중복체크
    Integer emailCheck(String email);

    //회원가입
    Integer saveUser(UserReqDto userReqDto);

    // 사용자/관리자 가입 시 권한 추가
    Integer saveRole(UserRoleReqDto userRoleReqDto);

    //조건(이름/전번/비번)에 맞는 아이디 출력
    UserResDto findId(UserReqDto userReqDto);

    //비밀번호 찾기
    UserResDto findPw(UserReqDto userReqDto);

    //비밀번호 수정
    Integer updatePw(UserResDto userResDto);

    //마마이페이지 조회
    UserResDto mypage(String id);

    // 마이페이지 수정
    Integer updateInfo(UserReqDto userReqDto);

    // 비밀번호 변경
    Integer updateMyPw(UserReqDto userReqDto);

    // 회원 탈퇴
    Integer deleteInfo(String id);

    //회원 권한 삭제
    Integer deleteRole(Integer user_id);


}
