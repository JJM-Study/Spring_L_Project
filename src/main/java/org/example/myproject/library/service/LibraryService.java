package org.example.myproject.library.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.library.dto.MyLibraryItemsDto;
import org.example.myproject.library.mapper.LibraryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryService {

    private final static Logger logger = LogManager.getLogger(LibraryService.class);

    @Autowired
    LibraryMapper libraryMapper;

    public List<MyLibraryItemsDto> selectMyLibraryItems(String userId) {
        return libraryMapper.selectMyLibraryItems(userId);
    }
}
