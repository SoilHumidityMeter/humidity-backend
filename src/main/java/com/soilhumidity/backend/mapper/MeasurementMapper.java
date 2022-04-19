package com.soilhumidity.backend.mapper;

import com.soilhumidity.backend.dto.measurement.MeasurementDto;
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

    public MeasurementDto map(Measurement measurement) {
        return new MeasurementDto(
                measurement.getId(),
                measurement.getHumidity(),
                measurement.getUserDevice().getId(),
                measurement.getCreatedAt(),
                seedRepository.getAllByHumidityDownGreaterThanEqualAndHumidityUpLessThanEqual(measurement.getHumidity(), measurement.getHumidity()).stream().map(seedFactory::createSeedDto).collect(Collectors.toList()),
                measurement.getPoint() != null ? SpatialFactory.createSpringPoint(measurement.getPoint()) : null
        );
    }
}
