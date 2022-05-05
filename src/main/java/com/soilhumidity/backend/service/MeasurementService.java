package com.soilhumidity.backend.service;

import com.soilhumidity.backend.config.security.JwtUtil;
import com.soilhumidity.backend.dto.PageFilter;
import com.soilhumidity.backend.dto.Response;
import com.soilhumidity.backend.dto.measurement.IpGeolocationResponse;
import com.soilhumidity.backend.dto.measurement.MeasurementDto;
import com.soilhumidity.backend.factory.NotificationFactory;
import com.soilhumidity.backend.mapper.MeasurementMapper;
import com.soilhumidity.backend.model.Measurement;
import com.soilhumidity.backend.repository.MeasurementRepository;
import com.soilhumidity.backend.repository.UserDeviceRepository;
import com.soilhumidity.backend.specs.factory.MeasurementSpecFactory;
import com.soilhumidity.backend.util.HttpClient;
import com.soilhumidity.backend.util.service.notification.NotificationException;
import com.soilhumidity.backend.util.service.notification.onesignal.OneSignalService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    private final UserDeviceRepository userDeviceRepository;

    private final MeasurementSpecFactory measurementSpecFactory;

    private final JwtUtil jwtUtil;

    private final MeasurementMapper measurementMapper;

    private final OneSignalService oneSignalService;

    private final NotificationFactory notificationFactory;

    private final HttpClient httpClient;

    @Value("${soilhm.ip-geolocation-api.url}")
    private String ipGeolocationApiUrl;

    @Transactional
    public void insertMeasurement(String deviceId, Double humidity, String ip) {

        var maybeDevice = userDeviceRepository.findByDeviceId(deviceId);

        if (maybeDevice.isEmpty()) {
            return;
        }

        var device = maybeDevice.get();
        var geoLoc = httpClient.readResult(
                httpClient.get(ipGeolocationApiUrl + ip,
                        Map.of("fields", "lat,lon")),
                IpGeolocationResponse.class);

        Point point = null;

        if (geoLoc.isOk()) {
            point = geoLoc.getData().asPoint();
        }

        measurementRepository.save(new Measurement(humidity, device, point));

        try {
            oneSignalService.send(notificationFactory.createPersonalNotification(
                    notificationFactory.createWebNotification(
                            device.getDeviceId() + " measured new humidity level, check measurements to see eligible seeds",
                            "Soil Cloud", device.getUser())
                    , Set.of(device.getUser())));
        } catch (NotificationException ignored) {

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
