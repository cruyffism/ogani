package com.minki.ogani.controller.admin;

import com.minki.ogani.dto.admin.ProductReqDto;
import com.minki.ogani.dto.admin.ProductResDto;
import com.minki.ogani.dto.page.Criteria;
import com.minki.ogani.dto.page.PageMaker;
import com.minki.ogani.service.admin.AdminProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminProductController {
    @Autowired
    private AdminProductService adminService;

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
                                  @RequestParam(value = "productName", required = false) String productName,
                                  @RequestParam(value = "productPrice", required = false) Integer productPrice,
                                  @RequestParam(value = "productClassification", required = false) String productClassification,
                                  @RequestParam(value = "productDescription", required = false) String productDescription,
                                  @RequestParam(value = "productWeight", required = false) Double productWeight,
                                  @RequestParam(value = "productQuantity", required = false) Long productQuantity, HttpServletResponse response) throws IOException {
        ProductReqDto productReqDto = new ProductReqDto();
        productReqDto.setProductName(productName);
        productReqDto.setProductPrice(productPrice);
        productReqDto.setProductClassification(productClassification);
        productReqDto.setProductDescription(productDescription);
        productReqDto.setProductWeight(productWeight);
        productReqDto.setProductQuantity(productQuantity);

        System.out.println("img: " + img);
        if (img != null && !img.isEmpty()) {
            productReqDto.setFileBytes(img.getBytes());
            productReqDto.setFileName(img.getOriginalFilename());
            productReqDto.setFileSize(img.getSize());
            productReqDto.setMimeType(img.getContentType());
        }

        System.out.println("productReqDto: "+productReqDto);
        // 상품 정보 등록
        Integer productRegister = adminService.productRegister(productReqDto);
        System.out.println("productRegister: "+productRegister);
        model.addAttribute("productRegister", productRegister);
        // 등록 후 alert 창 띄우기
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.println("<script>alert('상품이 등록되었습니다.');</script>");
        writer.flush();
        // 등록이 다 됐으면 상품 관리 리스트 화면으로!
        return "admin/productList";
    }

    // 이미지 조회 >> 플레이어리스트 화면에 선수 이미지를 전부 뿌려주는것
    @GetMapping("/productImage/{productId}")
    @ResponseBody
    public ResponseEntity<byte[]> productImage(@PathVariable Long productId) {
        ProductResDto productInfo = adminService.productInfo(productId);
        byte[] imageContent = productInfo.getFile_bytes();
        MediaType mediaType = MediaType.valueOf(productInfo.getMime_type());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
    }

    //상품 수정폼
    @GetMapping("/productEditForm/{productId}")
    public String productEditForm(Model model, @PathVariable Long productId){
        ProductResDto productInfo = adminService.productInfo(productId);
        model.addAttribute("productInfo", productInfo);
        return "admin/productEditForm";
    }

    //등록 상품 수정
    @PostMapping("/productUpdate")
    public String productUpdate(@RequestParam(value = "image", required = false) MultipartFile img,
                                @RequestParam(value = "productId", required = false) Long productId,
                                @RequestParam(value = "productName", required = false) String productName,
                                @RequestParam(value = "productPrice", required = false) Integer productPrice,
                                @RequestParam(value = "productClassification", required = false) String productClassification,
                                @RequestParam(value = "productDescription", required = false) String productDescription,
                                @RequestParam(value = "productWeight", required = false) Double productWeight,
                                @RequestParam(value = "productQuantity", required = false) Long productQuantity, HttpServletResponse response) throws IOException {

        ProductReqDto productReqDto = new ProductReqDto();
        productReqDto.setProductId(productId);
        productReqDto.setProductName(productName);
        productReqDto.setProductPrice(productPrice);
        productReqDto.setProductClassification(productClassification);
        productReqDto.setProductDescription(productDescription);
        productReqDto.setProductWeight(productWeight);
        productReqDto.setProductQuantity(productQuantity);

        System.out.println("img: " + img);
        if (img != null && !img.isEmpty()) {
            productReqDto.setFileBytes(img.getBytes());
            productReqDto.setFileName(img.getOriginalFilename());
            productReqDto.setFileSize(img.getSize());
            productReqDto.setMimeType(img.getContentType());
        }
        Integer result = adminService.productUpdate(productReqDto);
       if ( result == 0) {
           response.setContentType("text/html; charset=UTF-8");
           PrintWriter writer = response.getWriter();
           writer.println("<script>alert('상품 수정이 실패하였습니다.');</script>");
           writer.flush();
           return "admin/productEditForm";
       }
       else {
           response.setContentType("text/html; charset=UTF-8");
           PrintWriter writer = response.getWriter();
           writer.println("<script>alert('상품 수정을 완료했습니다.');</script>");
           writer.flush();
           return "admin/productList";
       }
    }

    //등록 상품 삭제
    @GetMapping("/productDelete/{productId}")
    public String productDelete(@PathVariable Long productId, HttpServletResponse response) throws IOException {
        Integer result = adminService.productDelete(productId);
        if(result ==0){
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('삭제에 실패하였습니다.');</script>");
            writer.flush();
            return "admin/productList";
        }
        else {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("<script>alert('삭제가 완료되었습니다.');</script>");
            writer.flush();
            return "admin/productList";
        }
    }

}


