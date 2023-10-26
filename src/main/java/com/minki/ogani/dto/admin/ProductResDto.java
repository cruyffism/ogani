package com.minki.ogani.dto.admin;

import lombok.Data;

//db에서 받는 데이터라 언더바 사용
@Data
public class ProductResDto {
    private Long product_id;
    private String product_name;
    private Integer product_price;
    private String product_classification;
    private String file_name;
    private Long file_size;
    private byte[] file_bytes;
    private String mime_type;
    private String product_description;
    private Long product_quantity;
    private Double product_weight;
}
