package com.soilhumidity.backend.dto.seed;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SeedDto {
    private String name;
    private String description;
    private String image;
    private String link;
    private Long id;
    private Double humidityUp;
    private Double humidityDown;


}
