package com.soilhumidity.backend.dto.measurement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WeatherForecastResponse {

    @JsonIgnore
    private Object coord;

    private List<WeatherResponse> weather;

    @JsonIgnore
    private String base;

    private TemperatureResponse main;

    @JsonIgnore
    private Long visibility;

    private WindResponse wind;

    @JsonIgnore
    private Object clouds;

    @JsonIgnore
    private Long dt;

    @JsonIgnore
    private Object sys;

    @JsonIgnore
    private Long timezone;

    @JsonIgnore
    private Long id;

    private String name;

    @JsonIgnore
    private Integer cod;

}
