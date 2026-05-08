package org.example.myproject.gis.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


// 2026/05/06 추가
@Getter
@Setter
@RequiredArgsConstructor
public class InputGISDto {
    private Double lat;
    private Double lon;
    private int k;

}
