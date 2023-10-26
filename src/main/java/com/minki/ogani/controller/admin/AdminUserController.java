package com.minki.ogani.controller.admin;

import com.minki.ogani.dto.admin.ProductResDto;
import com.minki.ogani.dto.page.Criteria;
import com.minki.ogani.dto.page.PageMaker;
import com.minki.ogani.dto.user.UserResDto;
import com.minki.ogani.service.admin.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminUserController {
    @Autowired
    AdminUserService adminUserService;

    //회원 관리 첫 페이지
    @GetMapping("/userList")
    public String userList() {
        return"admin/userList";
    }

    //회원 관리 리스트
    @GetMapping("/userListAjax")
    public String userListAjax(Model model, @ModelAttribute Criteria criteria) {
        List<UserResDto> userList = adminUserService.userList(criteria);
        PageMaker pageMaker = new PageMaker(); //PageMaker 클래스파일을 사용하기위해 선언한 것 >> 선언해주면 PageMaker 클래스파일의 변수들을 get, set해서 사용가능하다.
        pageMaker.setCriteria(criteria); // criteria라는 변수에다가 우리가 @ModelAttribute를 통해 매개변수로 받은 criteria 값을 저장
        pageMaker.setTotalCount(adminUserService.userCount(criteria));
        System.out.println("pageMaker : " + pageMaker);
        model.addAttribute("PageMaker", pageMaker);
        model.addAttribute("userList", userList);
        return "admin/userListAjax";
    }
}
