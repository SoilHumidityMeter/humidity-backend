package com.soilhumidity.backend.dto.seed;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SeedRequest {

    @ApiModelProperty(example = "abc.jpg")
    private String name;

    @ApiModelProperty(example = "abc.jpg")
    private String description;

    @ApiModelProperty(example = "abc.jpg")
    private String link;

    @ApiModelProperty(example = "abc.jpg")
    private Double humidityUp;

    @ApiModelProperty(example = "abc.jpg")
    private Double humidityDown;
}
