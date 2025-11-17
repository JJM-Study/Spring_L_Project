package org.example.myproject.file.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FileMasterDto {
    private Long fileId;
    private String domainType;
    private Long domainId;
//    private String userId;
    private String filePurpose;
    private String fileStatus;
    private String approvalStatus;
    private String originalFilename;
    private String storedFileName;
    private LocalDateTime regDt;
    private LocalDateTime updDt;
}
