package org.example.myproject.file.dto;

import lombok.Data;
import org.springframework.core.io.Resource;


@Data
public class FileDownloadDto {
    private final Resource resource;
    private final String originalFilename;

    public FileDownloadDto(String originalFilename, Resource resource) {
        this.resource = resource;
        this.originalFilename = originalFilename;
    }
}
