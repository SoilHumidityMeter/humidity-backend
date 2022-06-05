package com.soilhumidity.backend.dto.measurement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WeatherResponse {

    private String id;

    private String main;

    private String description;

    private String icon;
}
