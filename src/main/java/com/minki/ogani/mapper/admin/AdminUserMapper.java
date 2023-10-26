package com.minki.ogani.mapper.admin;

import com.minki.ogani.dto.admin.ProductResDto;
import com.minki.ogani.dto.page.Criteria;
import com.minki.ogani.dto.user.UserResDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminUserMapper {

    //회원 리스트 조회
    List<UserResDto> userList(Criteria criteria);

    // db에 있는 회원수
    Integer userCount(Criteria criteria);
}
