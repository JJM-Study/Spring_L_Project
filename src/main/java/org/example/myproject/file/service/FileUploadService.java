package org.example.myproject.file.service;

import org.example.myproject.file.mapper.FileUploadMapper;
import org.example.myproject.file.dto.FileUploadDto;
import org.example.myproject.file.dto.FileUploadRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileUploadService {

    @Autowired
    private FileUploadMapper fileUploadMapper;

    public int uploadFileFromUrl(FileUploadRequestDto dto) {
        return fileUploadMapper.fileUploadFromUrl(dto);

    }

    public int uploadFile(FileUploadDto dto) {

        return fileUploadMapper.fileUpload(dto);
    }

}