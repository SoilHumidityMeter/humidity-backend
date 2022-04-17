package com.soilhumidity.backend.dto.image;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PointZ {

    @ApiModelProperty(example = "-1.0")
    private Float x;

    @ApiModelProperty(example = "-1.0")
    private Float y;

    @ApiModelProperty(example = "-1.0")
    private Float z;

    @ApiModelProperty(example = "-1.0")
    private Float w;

    public static PointZ of(Float x, Float y, Float z) {
        return new PointZ(x, y, z, 1.0f);
    }
}
