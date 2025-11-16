package org.example.myproject.library.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MyLibraryItemsDto {
    private Long lyb_no;
    private String user_id;
    private Long prod_no;
    private LocalDate reg_dt;

}
