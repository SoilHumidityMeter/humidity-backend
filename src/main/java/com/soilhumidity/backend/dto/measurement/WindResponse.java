package com.soilhumidity.backend.dto.measurement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WindResponse {

    private Double speed;

    private Double deg;

    private Double gust;
}
