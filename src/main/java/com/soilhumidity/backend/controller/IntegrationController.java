package com.soilhumidity.backend.controller;

import com.soilhumidity.backend.service.MeasurementService;
import com.soilhumidity.backend.util.LoggableFuture;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@AllArgsConstructor
@ApiIgnore
@RequestMapping(MeasurementController.TAG)
public class IntegrationController {

    private final MeasurementService measurementService;

    @PostMapping
    public ResponseEntity<Void> insertMeasurement(
            @RequestParam String deviceId,
            @RequestParam Double humidity,
            @RequestParam String ip
    ) {
        LoggableFuture.runAsync(() -> measurementService.insertMeasurement(deviceId, humidity, ip));

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
