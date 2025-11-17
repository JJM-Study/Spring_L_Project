package org.example.myproject.file.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.common.enums.DomainType;
import org.example.myproject.error.BusinessException;
import org.example.myproject.error.ErrorCode;
import org.example.myproject.file.dto.FileDownloadDto;
import org.example.myproject.file.dto.FileMasterDto;
import org.example.myproject.file.mapper.FileMapper;
import org.example.myproject.file.mapper.FileUploadMapper;
import org.example.myproject.file.dto.FileUploadDto;
import org.example.myproject.file.dto.FileUploadRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

@Service
public class FileService {

    @Autowired
    private FileUploadMapper fileUploadMapper;

    private final FileMapper fileMapper;

    private final Path rootDir;

    private final static Logger logger = LogManager.getLogger(FileService.class);

    public FileService(@Value("${file.upload-dir}") String uploadDir, FileMapper fileMapper) {
        this.rootDir = Paths.get(uploadDir);
        this.fileMapper = fileMapper;
    }


    public int uploadFileFromUrl(FileUploadRequestDto dto) {
        return fileUploadMapper.fileUploadFromUrl(dto);

    }

    public int uploadFile(FileUploadDto dto) {

        return fileUploadMapper.fileUpload(dto);
    }

    @Transactional(readOnly = true)
    public FileDownloadDto loadFile(Long fileId) {
        FileMasterDto fileMasterDto = fileMapper.selectFileById(fileId);

        if(fileMasterDto == null) {
            throw new BusinessException(ErrorCode.FILE_NOT_FOUND);
        }

//        Path filePath = rootDir + fileMasterDto.getDomainType() + fileMasterDto.getRegDt();
        Path filePath = getFullPath(fileMasterDto);
        logger.info("filePath : " + filePath);
        try {
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return new FileDownloadDto(fileMasterDto.getOriginalFilename(), resource);
            } else {
                throw new BusinessException(ErrorCode.FILE_NOT_FOUND);
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("MalformedURLException {}" + e.getMessage());
        }

    }


    // 루트 디렉토리 / 도메인 / 도메인 ID / 날짜 조합.
    public Path getFullPath(FileMasterDto fileMasterDto) {


        String datePath = fileMasterDto.getRegDt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        DomainType domainType = DomainType.valueOf(fileMasterDto.getDomainType());
        String domainName = domainType.getDirName();

        return this.rootDir
                .resolve(domainName)
                .resolve(fileMasterDto.getDomainId().toString())
                .resolve(datePath)
                .resolve(fileMasterDto.getStoredFileName());
    }

}