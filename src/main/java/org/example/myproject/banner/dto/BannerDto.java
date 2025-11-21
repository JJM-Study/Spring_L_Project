package org.example.myproject.banner.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BannerDto {
    private Integer bannerId;
    private String linkUrl;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String originalFilename;
    private String storedFilename;
    private String domainUrl;


    public String getImageUrl() {
        return this.domainUrl + "/" + this.storedFilename;
    }

}
