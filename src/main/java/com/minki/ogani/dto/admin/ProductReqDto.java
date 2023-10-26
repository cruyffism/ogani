package com.minki.ogani.dto.admin;

import lombok.Data;

@Data
public class ProductReqDto {
    private Long productId;
    private String productName;
    private Integer productPrice;
    private String productClassification;
    private String fileName;
    private Long fileSize;
    private byte[] fileBytes;
    private String mimeType;
    private String productDescription;
    private Long productQuantity;
    private Double productWeight;

}
