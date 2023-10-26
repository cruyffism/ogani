package com.minki.ogani.service.admin;

import com.minki.ogani.dto.page.Criteria;
import com.minki.ogani.dto.user.UserResDto;
import com.minki.ogani.mapper.admin.AdminUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserService {
    @Autowired
    AdminUserMapper adminUserMapper;

    //회원 리스트 조회
    public List<UserResDto> userList(Criteria criteria) {
        return adminUserMapper.userList(criteria);
    }

    // db에 있는 회원수
    public Integer userCount(Criteria criteria){
        return adminUserMapper.userCount(criteria);
    }
}
