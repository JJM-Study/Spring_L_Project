package org.example.myproject.library.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.myproject.library.dto.MyLibraryItemsDto;

import java.util.List;

@Mapper
public interface LibraryMapper {
    List<MyLibraryItemsDto> selectMyLibraryItems(String userId);

    int insertLibraryItems(List<Long> prodNos, String userId);


    /*--- 2025/11/27 테스트용 추가 ---*/
    void deleteLibraries(List<String> users);
}
