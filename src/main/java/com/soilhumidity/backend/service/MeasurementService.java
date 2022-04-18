package com.soilhumidity.backend.service;

import com.soilhumidity.backend.config.security.JwtUtil;
import com.soilhumidity.backend.dto.PageFilter;
import com.soilhumidity.backend.dto.Response;
import com.soilhumidity.backend.dto.measurement.MeasurementDto;
import com.soilhumidity.backend.factory.NotificationFactory;
import com.soilhumidity.backend.mapper.MeasurementMapper;
import com.soilhumidity.backend.model.Measurement;
import com.soilhumidity.backend.repository.MeasurementRepository;
import com.soilhumidity.backend.repository.UserDeviceRepository;
import com.soilhumidity.backend.specs.factory.MeasurementSpecFactory;
import com.soilhumidity.backend.util.service.notification.NotificationException;
import com.soilhumidity.backend.util.service.notification.onesignal.OneSignalService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@AllArgsConstructor
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    private final UserDeviceRepository userDeviceRepository;

    private final MeasurementSpecFactory measurementSpecFactory;

    private final JwtUtil jwtUtil;

    private final MeasurementMapper measurementMapper;

    private final OneSignalService oneSignalService;

    private final NotificationFactory notificationFactory;

    @Transactional
    public void insertMeasurement(String deviceId, Double humidity) {

        var maybeDevice = userDeviceRepository.findByDeviceId(deviceId);

        if (maybeDevice.isEmpty()) {
            return;
        }

        var device = maybeDevice.get();

        measurementRepository.save(new Measurement(humidity, device));

        try {
            oneSignalService.send(notificationFactory.createPersonalNotification(
                    notificationFactory.createWebNotification(
                            device.getDeviceId() + " measured new humidity level, check measurements to see eligible seeds",
                            "Soil Cloud", device.getUser())
                    , Set.of(device.getUser())));
        } catch (NotificationException e) {
            e.printStackTrace();
        }
    }

    @Transactional(readOnly = true)
    public Response<Page<MeasurementDto>> getMeasurements(PageFilter pageFilter, String token) {

        var userId = jwtUtil.getUserId(token);

        var page = measurementRepository.findAll(
                measurementSpecFactory.getByUserId(userId),
                pageFilter.asPageable());

        return Response.ok(page.map(measurementMapper::map));
    }
}
