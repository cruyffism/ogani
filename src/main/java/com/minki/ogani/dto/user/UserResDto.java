package com.minki.ogani.dto.user;

import lombok.Data;

//select가 실행되면 디비버에 저장된 변수명으로 된 결과값을 받기 위해
@Data
public class UserResDto {
    private Integer user_id;
    private String name;
    private String id;
    private String phonenumber;
    private String postcode;
    private String address;
    private String detail_address;
    private String extra_address;
    private String birthday;
    private String email;
    private String password;
    private Integer type;
}
