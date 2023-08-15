package com.minki.ogani.dto.user;


import lombok.Data;

//변수를 여러개 담아서 보낼 때 dto를 만든다.
//@Data 설정시 set, get 자동으로 생성해서 편리함
@Data
public class UserReqDto {
    private Integer userId;
    private String name;
    private String id;
    private String phonenumber;
    private String postcode;
    private String address;
    private String detailAddress;
    private String extraAddress;
    private String birthday;
    private String email;
    private String password;
    private Integer type;
}

