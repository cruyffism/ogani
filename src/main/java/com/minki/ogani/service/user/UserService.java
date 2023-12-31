package com.minki.ogani.service.user;

import com.minki.ogani.dto.user.UserReqDto;
import com.minki.ogani.dto.user.UserResDto;
import com.minki.ogani.dto.user.UserRoleReqDto;
import com.minki.ogani.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    // Autowired를 이용해서 UserMapper와 연결했다. 그래야지 메소드 사용이 가능하기 때문에!
    @Autowired
    private UserMapper userMapper;

    //비번 암호화 객체
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //아이디 중복 체크
    // UserMapper의 idCheck 메소드 호출
    // 호출하면 메소드의 리턴을 받겠다. 이게 결국 카운트 했던 1 or 0이 넘어오는 것임! 리턴값은 resultType
    public Integer idCheck(String id) {
        return userMapper.idCheck(id);
    }

    //이메일 중복 체크
    public Integer emailCheck(String email) { return userMapper.emailCheck(email);}

    //회원가입
    public Integer saveUser(UserReqDto userReqDto) {
        // password는 암호화해서 DB에 저장
        userReqDto.setPassword(passwordEncoder.encode(userReqDto.getPassword()));
        userMapper.saveUser(userReqDto);
        // 권한 저장
        UserRoleReqDto userRoleReqDto = new UserRoleReqDto();
        userRoleReqDto.setUserId(userReqDto.getUserId());
        userRoleReqDto.setRoleId(userReqDto.getType());
        return userMapper.saveRole(userRoleReqDto);

    }

    //조건(이름/전번/비번)에 맞는 아이디 출력
    public UserResDto findId(UserReqDto userReqDto) {

        return userMapper.findId(userReqDto);
    }

    // 비밀번호 찾기
    public UserResDto findPw(UserReqDto userReqDto) {

        return userMapper.findPw(userReqDto);
    }

    //비밀번호 수정
    public Integer updatePw(UserResDto userResDto) {
        return userMapper.updatePw(userResDto);
    }

    //마마이페이지 조회
    public UserResDto mypage(String id) {
        return userMapper.mypage(id);
    }

    // 마이페이지 수정
    public Integer updateInfo(UserReqDto userReqDto) {
        return userMapper.updateInfo(userReqDto);
    }

    // 비밀번호 변경
    public Integer updatePw(UserReqDto userReqDto) {
        return userMapper.updateMyPw(userReqDto);
    }

    //회원 탈퇴
    public Integer deleteInfo(String id) {
        return userMapper.deleteInfo(id);
    }

    //회원 권한 삭제
    public Integer deleteRole(Integer user_id) {
        return userMapper.deleteRole(user_id);
    }

}
