package org.example.myproject.common.dto;

import org.example.myproject.common.dto.UploadCallbackDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UploadCallbackMapper {
    int insertCallback(UploadCallbackDto ordnum);
}
