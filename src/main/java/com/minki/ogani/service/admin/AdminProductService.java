package com.minki.ogani.service.admin;

import com.minki.ogani.dto.admin.ProductReqDto;
import com.minki.ogani.dto.admin.ProductResDto;
import com.minki.ogani.dto.page.Criteria;
import com.minki.ogani.mapper.admin.AdminProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminProductService {

    @Autowired
    private AdminProductMapper adminMapper;

    // 품목리스트 조회
    public List<ProductResDto> productList(Criteria criteria){
       return adminMapper.productList(criteria);
    }

    // db에 있는 product 총 개수(품목리스트 개수)
    public Integer productCount(Criteria criteria){
        return adminMapper.productCount(criteria);
    }

    //등록 상품 저장
    public Integer productRegister(ProductReqDto productReqDto) {
        return adminMapper.productRegister(productReqDto);
    }

    //단건 상품 조회
    public ProductResDto productInfo(Long productId) {
        return adminMapper.productInfo(productId);
    }

    //등록 상품 수정
    public Integer productUpdate(ProductReqDto productReqDto){
        return adminMapper.productUpdate(productReqDto);
    }

    //등록 상품 삭제
    public Integer productDelete(Long productId){
        return adminMapper.productDelete(productId);
    }

}
