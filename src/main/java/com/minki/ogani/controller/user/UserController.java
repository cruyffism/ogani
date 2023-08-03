package com.minki.ogani.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping ("/user") // 전체 백엔드 주소 경로  = prefix
public class UserController {

    // 회원가입 페이지(빈곽)
    @GetMapping("/signupForm")
    public String signupForm(){
        return "user/signupForm";
    }


    // 회원가입 페이지 ajax
    @GetMapping("/signupAjax/{type}")
    public String signupAjax(Model model, @PathVariable Integer type) {
        if(type == 1) {
            model.addAttribute("type", type);
            return "user/userSignupAjax";
        } else {
            model.addAttribute("type", type);
            return "user/adminSignupAjax";
        }
    }



}
