package org.example.myproject.file.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.myproject.file.dto.FileDownloadDto;
import org.example.myproject.file.dto.FileMasterDto;

@Mapper
public interface FileMapper {

    FileMasterDto selectFileById(Long fileId);
}
