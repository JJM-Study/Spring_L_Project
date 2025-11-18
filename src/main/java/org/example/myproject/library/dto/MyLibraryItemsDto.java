package org.example.myproject.library.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MyLibraryItemsDto {
    private Long lybNo;
    private String userId;
    private String sellerId;
//    private String domainType; // 나중에 필요하면 추가.
//    private String filePurpose;
    private String originalFilename;
    private String fileStatus;
    private String prodName;
    private Long prodNo;
    private LocalDate lybRegDt;
    private String imageUrl;

}
