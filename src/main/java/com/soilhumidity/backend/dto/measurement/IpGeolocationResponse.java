package com.soilhumidity.backend.dto.measurement;

import com.soilhumidity.backend.dto.generic.PointDto;
import com.soilhumidity.backend.factory.SpatialFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IpGeolocationResponse {

    private boolean status;

    private Double lon;

    private Double lat;

    public Point asPoint() {
        if (lon == null || lat == null) {
            return null;
        }
        return SpatialFactory.createDatabasePoint(new PointDto(lon, lat));
    }

}
