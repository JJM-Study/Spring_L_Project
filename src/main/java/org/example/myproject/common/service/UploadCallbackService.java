package org.example.myproject.common.service;

import org.example.myproject.common.mapper.UploadCallbackMapper;
import org.example.myproject.common.dto.UploadCallbackDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UploadCallbackService {
    @Autowired
    private UploadCallbackMapper uploadCallbackMapper;

    public int insertCallback(UploadCallbackDto dto) {
        return uploadCallbackMapper.insertCallback(dto);
    }

}
