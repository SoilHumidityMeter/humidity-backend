package com.soilhumidity.backend.service;

import com.soilhumidity.backend.config.security.JwtUtil;
import com.soilhumidity.backend.dto.PageFilter;
import com.soilhumidity.backend.dto.Response;
import com.soilhumidity.backend.dto.measurement.IpGeolocationResponse;
import com.soilhumidity.backend.dto.measurement.MeasurementDto;
import com.soilhumidity.backend.dto.measurement.WeatherForecastResponse;
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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Value("${soilhm.weather-api.url}")
    private String weatherApiUrl;

    @Value("${soilhm.weather-api.api-key}")
    private String weatherKey;

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

        var weather = httpClient.readResult(
                httpClient.get(weatherApiUrl,
                        Map.of("units", "metric",
                                "lat", 38.32292,
                                "lon", 26.76403,
                                "appid", weatherKey)),
                WeatherForecastResponse.class);

        var page = measurementRepository.findAll(
                measurementSpecFactory.getByUserId(userId),
                pageFilter.asPageable());

        return Response.ok(page.map(mesa -> measurementMapper.map(mesa, weather.getData())));
    }

    @Transactional(readOnly = true)
    public Response<List<MeasurementDto>> getMeasurementsWhole() {
        var all = measurementRepository.findAll();

        return Response.ok(all.stream().map(measurementMapper::map).collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public Response<MeasurementDto> getAvgMeasurements(String city) {
        var avg = measurementRepository.getAverageHumidity();

        return Response.ok(measurementMapper.map(avg));
    }
}
