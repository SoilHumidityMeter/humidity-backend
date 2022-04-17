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
    private Double x;

    @ApiModelProperty(example = "-1.0")
    private Double y;

    @ApiModelProperty(example = "-1.0")
    private Double z;

    @ApiModelProperty(example = "-1.0")
    private Double w;

    public static PointZ of(Double x, Double y, Double z) {
        return new PointZ(x, y, z, 1.0d);
    }
}
