package com.minki.ogani.controller.user;

import com.minki.ogani.dto.user.UserReqDto;
import com.minki.ogani.dto.user.UserResDto;
import com.minki.ogani.service.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("/user") // 전체 백엔드 주소 경로  = prefix
public class UserController {
    @Autowired
    private UserService userService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    // 회원가입 페이지(빈곽)
    // 접근제한자 리턴값(타입) 메소드명(매개변수)
    @GetMapping("/signupForm")
    public String signupForm() {
        return "user/signupForm";
    }


    // 회원가입 페이지 ajax
    @GetMapping("/signupAjax/{type}")
    public String signupAjax(Model model, @PathVariable Integer type) {
        if (type == 1) {
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
        Integer result = userService.saveUser(userReqDto);

        if (result == 1) {
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

    // 로그인(로그인 화면 이동)
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "user/loginForm";
    }

    // 아이디 찾기 폼(빈 곽)
    @GetMapping("/findIdForm")
    public String findIdForm() {
        return "user/findIdForm";
    }

    //조건(이름/전번/비번)에 맞는 아이디 출력
    @PostMapping("/findId")
    public String findId(Model model, @ModelAttribute UserReqDto userReqDto, HttpServletResponse response) throws IOException {
        UserResDto userResDto = userService.findId(userReqDto);
        if (userResDto == null) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('해당정보가 존재하지 않습니다.');</script>");
            writer.flush();
            return "user/findIdForm";
        }
        model.addAttribute("id", userResDto);
        return "user/findId";
    }

    // 비밀번호 찾기 폼(빈 곽)
    @GetMapping("/findPwForm")
    public String findPwForm() {
        return "user/findPwForm";
    }

    // 비밀번호 찾기
    @PostMapping("/findPw")
    public String findPw(Model model, @ModelAttribute UserReqDto userReqDto, HttpServletResponse response) throws IOException {
        UserResDto userResDto = userService.findPw(userReqDto); // 1. 아이디, 이메일이 일치하는 회원정보가 있는지 체크
        if (userResDto == null) { // 회원정보가 없다면
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('해당정보가 존재하지 않습니다.');</script>");
            writer.flush();
            return "user/findPwForm";
        } else { // 회원정보가 있다면

            // 2. 임시 비밀번호를 만들어서 이메일로 전송
            // 2-1. 임시비밀번호 생성
            String tempPw = getTempPassword();

            // 2-2. 이메일로 임시비밀번호 내용 전송
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(userReqDto.getEmail());
            message.setSubject("[ogani] 임시 비밀번호 안내드립니다.");
            message.setText("안녕하세요. \n\n" + userReqDto.getId() + "님의 임시비밀번호는 " + tempPw + " 입니다.\n\n 로그인 후에 비밀번호를 변경을 해주세요");
            message.setFrom(from);
            message.setReplyTo(from);
            javaMailSender.send(message);

            // 3. 임시 비밀번호 db에 수정
            userResDto.setPassword(passwordEncoder.encode(tempPw));
            Integer updatePw = userService.updatePw(userResDto);

            // 4. 성공하면 alert창 띄우기
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('가입 된 이메일로 임시 비밀번호가 전송되었습니다.');</script>");
            writer.flush();
            return "user/loginForm";
        }
    }

    // 임시 비밀번호 랜덤으로 만들었음
    public String getTempPassword() {
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }

    //마이페이지
    @GetMapping("/mypage")
    public String mypage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // 백엔드에서 로그인 정보 가져와서 아이디 값을 조회
        String id = auth.getName();
        UserResDto userResDto = userService.mypage(id);
        model.addAttribute("info", userResDto);
        return "user/mypage";
    }

    // 회원정보 변경화면
    @GetMapping("/updateInfoForm")
    public String updateInfoForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // 백엔드에서 로그인 정보 가져와서 아이디 값을 조회
        String id = auth.getName();
        UserResDto userResDto = userService.mypage(id);
        model.addAttribute("info", userResDto);
        return "user/updateInfoForm";
    }


}
