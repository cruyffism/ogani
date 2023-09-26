package com.minki.ogani.dto.admin;

import lombok.Data;

@Data
public class ProductReqDto {
    private Integer productId;
    private String productName;
    private Integer productPrice;
    private String productClassification;
    private String fileName;
    private Long fileSize;
    private byte[] fileBytes;
    private String mimeType;
    private String productDescription;
    private long productQuantity;

}
