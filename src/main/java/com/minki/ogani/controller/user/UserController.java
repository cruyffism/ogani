package com.minki.ogani.controller.user;

import com.minki.ogani.dto.user.UserReqDto;
import com.minki.ogani.service.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping ("/user") // 전체 백엔드 주소 경로  = prefix
public class UserController {
    @Autowired
    private UserService userService;

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

    // 아이디 중복 체크 (restApi라고 부른다. >> 결과값이 html이 아니라 json으로 가는거! 보통 실무에서 쓰는 방식)
    @GetMapping("/idCheckAjax") //get 방식의 ""/idCheckAjax"라는 메소드라고 지정
    @ResponseBody // 리스폰스바디와 json은 한 쌍 !! dataType:"json" >> 리턴을 새로운 html이 아닌 데이터 자체값을 보내려고
    public Integer idCheck(@RequestParam String id) { // RequestParam을 이용해서 String이란 타입으로 id라는 변수를 받을거다
        int cnt = userService.idCheck(id);
        return cnt; // cnt라는 이름으로 결과값을 idCheckAjax가 호출한 function으로 보내면 그값을 보고 alert창을 띄워준다.
    }

    // 이메일 중복 체크 (restApi라고 부른다. >> 결과값이 html이 아니라 json으로 가는거! 보통 실무에서 쓰는 방식)
    @GetMapping("/emailCheckAjax")
    @ResponseBody
    public Integer emailCheck(@RequestParam String email) {
        int cnt = userService.emailCheck(email);
        return cnt;
    }

    //회원가입
    @PostMapping("/signup")
    public String saveUser(Model model, @ModelAttribute UserReqDto userReqDto, HttpServletResponse response) throws IOException {
        Integer result =  userService.saveUser(userReqDto);

        if(result == 1){
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('회원가입되었습니다.');</script>");
            writer.flush();
            return "user/loginForm";
        } else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('회원가입이 실패되었습니다.');</script>");
            writer.flush();
            return "user/signupForm";
        }
    }

    // 로그인
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception, Model model){
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "user/loginForm";
    }



}
