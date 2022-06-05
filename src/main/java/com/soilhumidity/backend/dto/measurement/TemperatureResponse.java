package com.soilhumidity.backend.dto.measurement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TemperatureResponse {

    private Double temp;

    private Double feels_like;

    private Double temp_min;

    private Double temp_max;

    private Double pressure;

    private Double humidity;

    private Double sea_level;

    private Double grnd_level;

}
