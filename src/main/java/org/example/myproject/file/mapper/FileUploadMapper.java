package org.example.myproject.file.mapper;
import org.example.myproject.file.dto.FileUploadDto;
import org.example.myproject.file.dto.FileUploadRequestDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileUploadMapper {
    int fileUploadFromUrl(FileUploadRequestDto dto);

    int fileUpload(FileUploadDto dto);

}
