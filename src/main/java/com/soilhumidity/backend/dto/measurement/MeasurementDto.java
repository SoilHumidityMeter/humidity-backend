package com.soilhumidity.backend.dto.measurement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.soilhumidity.backend.dto.seed.SeedDto;
import com.soilhumidity.backend.dto.user.UserDeviceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.geo.Point;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeasurementDto {

    private Long id;

    private Double humidity;

    private UserDeviceDto userDevice;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date createdAt;

    private List<SeedDto> eligibleSeeds;

    private Point point;

    @Setter
    private WeatherForecastResponse weather;
}
