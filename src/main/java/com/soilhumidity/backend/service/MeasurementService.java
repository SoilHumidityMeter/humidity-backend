package com.soilhumidity.backend.service;

import com.soilhumidity.backend.model.Measurement;
import com.soilhumidity.backend.repository.MeasurementRepository;
import com.soilhumidity.backend.repository.UserDeviceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    private final UserDeviceRepository userDeviceRepository;

    @Transactional
    public void insertMeasurement(String deviceId, Double humidity) {

        var maybeDevice = userDeviceRepository.findByDeviceId(deviceId);

        if (maybeDevice.isEmpty()) {
            return;
        }

        var device = maybeDevice.get();

        measurementRepository.save(new Measurement(humidity, device));
    }
}
