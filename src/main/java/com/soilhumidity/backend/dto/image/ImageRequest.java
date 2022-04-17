package com.soilhumidity.backend.dto.image;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageRequest {

    @ApiModelProperty(example = "abc.jpg")
    private String name;

    private PointZ point;
}
