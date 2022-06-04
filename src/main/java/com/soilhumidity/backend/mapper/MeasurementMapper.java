package com.soilhumidity.backend.mapper;

import com.soilhumidity.backend.dto.measurement.MeasurementDto;
import com.soilhumidity.backend.dto.user.UserDeviceDto;
import com.soilhumidity.backend.factory.SeedFactory;
import com.soilhumidity.backend.factory.SpatialFactory;
import com.soilhumidity.backend.model.Measurement;
import com.soilhumidity.backend.repository.SeedRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MeasurementMapper {

    private final SeedRepository seedRepository;

    private final SeedFactory seedFactory;

    public MeasurementDto map(Double avg) {
        return new MeasurementDto(
                null,
                avg,
                null,
                null,
                seedRepository.getAllByHumidityDownLessThanEqualAndHumidityUpGreaterThanEqual(avg, avg).stream().map(seedFactory::createSeedDto).collect(Collectors.toList()),
                null
        );
    }

    public MeasurementDto map(Measurement measurement) {
        return new MeasurementDto(
                measurement.getId(),
                measurement.getHumidity(),
                new UserDeviceDto(measurement.getUserDevice().getDeviceId(), measurement.getUserDevice().getId(), measurement.getCreatedAt()),
                measurement.getCreatedAt(),
                seedRepository.getAllByHumidityDownLessThanEqualAndHumidityUpGreaterThanEqual(measurement.getHumidity(), measurement.getHumidity()).stream().map(seedFactory::createSeedDto).collect(Collectors.toList()),
                measurement.getPoint() != null ? SpatialFactory.createSpringPoint(measurement.getPoint()) : null
        );
    }
}
