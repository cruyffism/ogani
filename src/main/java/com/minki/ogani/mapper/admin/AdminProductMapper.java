package com.minki.ogani.mapper.admin;

import com.minki.ogani.dto.admin.ProductReqDto;
import com.minki.ogani.dto.admin.ProductResDto;
import com.minki.ogani.dto.page.Criteria;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

// id 중복체크
// 리턴값(타입) 메소드(xml이랑연결하기 위해서 쓴다! xml 아이디와 같아야함) 이름(타입 변수명)
// select 제외하고는 전부 리턴값(타입)은 기본적으로 Integer

@Mapper
public interface AdminProductMapper {

    // 품목리스트 조회
    List<ProductResDto> productList(Criteria criteria);

    // db에 있는 product 총 개수(품목리스트 개수)
    Integer productCount(Criteria criteria);


    //등록 상품 저장
    Integer productRegister(ProductReqDto productReqDto);

    //단건 상품 조회
    ProductResDto productInfo(Long productId);

    //등록 상품 수정
    Integer productUpdate(ProductReqDto productReqDto);

    //등록 상품 삭제
    Integer productDelete(Long productId);
}
