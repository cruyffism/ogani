package com.minki.ogani.controller.admin;

import com.minki.ogani.dto.admin.ProductReqDto;
import com.minki.ogani.dto.admin.ProductResDto;
import com.minki.ogani.dto.page.Criteria;
import com.minki.ogani.dto.page.PageMaker;
import com.minki.ogani.service.admin.AdminService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    //상품관리 첫 페이지!!
    @GetMapping("/productList")
    public String productList() {
        return "admin/productList";
    }

    //상품관리 리스트
    @GetMapping("/productListAjax")
    public String productListAjax(Model model, @ModelAttribute Criteria criteria) {
        List<ProductResDto> productList = adminService.productList(criteria);
        PageMaker pageMaker = new PageMaker(); //PageMaker 클래스파일을 사용하기위해 선언한 것 >> 선언해주면 PageMaker 클래스파일의 변수들을 get, set해서 사용가능하다.
        pageMaker.setCriteria(criteria); // criteria라는 변수에다가 우리가 @ModelAttribute를 통해 매개변수로 받은 criteria 값을 저장
        pageMaker.setTotalCount(adminService.productCount(criteria));
        System.out.println("pageMaker : " + pageMaker);
        model.addAttribute("PageMaker", pageMaker);
        model.addAttribute("productList", productList);
        return "admin/productListAjax";
    }

    //상품등록페이지(빈곽)
    @GetMapping("/productRegisterForm")
    public String productRegisterForm() {
        return "admin/productRegisterForm";
    }

    //등록 상품 저장
    @PostMapping("/productRegister")
    public String productRegister(Model model, @RequestParam(value = "image", required = false) MultipartFile img,
                                  @RequestParam(value = "product_name", required = false) String productName,
                                  @RequestParam(value = "product_price", required = false) Integer productPrice,
                                  @RequestParam(value = "product_classification", required = false) String productClassification,
                                  @RequestParam(value = "product_description", required = false) String productDescription,
                                  @RequestParam(value = "product_quantity", required = false) Long productQuantity, HttpServletResponse response) throws IOException {
        ProductReqDto productReqDto = new ProductReqDto();
        productReqDto.setProductName(productName);
        productReqDto.setProductPrice(productPrice);
        productReqDto.setProductClassification(productClassification);
        productReqDto.setProductDescription(productDescription);
        productReqDto.setProductQuantity(productQuantity);

        if (img != null && !img.isEmpty()) {
            productReqDto.setFileBytes(img.getBytes());
            productReqDto.setFileName(img.getOriginalFilename());
            productReqDto.setFileSize(img.getSize());
            productReqDto.setMimeType(img.getContentType());
        }

        // 상품 정보 수정
        Integer productRegister = adminService.productRegister(productReqDto);
        model.addAttribute("productRegister", productRegister);
        // 수정 후 alert 창 띄우기
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.println("<script>alert('상품 정보가 수정되었습니다.');</script>");
        writer.flush();
        // 수정이 다 됐으면 상품 관리 리스트 화면으로!
        return "admin/productList";
    }

}


