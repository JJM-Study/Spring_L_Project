package org.example.myproject.product.dto;

import lombok.Data;

@Data
public class ProductCheckFileDto {
    private Long prodNo;
    private Long fileId;
    private String originalFilename;
    private String fileStatus;
    private String filePurpose;
    private String approvalStatus;

}
