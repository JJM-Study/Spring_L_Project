package org.example.myproject.library.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.myproject.library.dto.MyLibraryItemsDto;

import java.util.List;

@Mapper
public interface LibraryMapper {
    List<MyLibraryItemsDto> selectMyLibraryItems(String userId);
}
