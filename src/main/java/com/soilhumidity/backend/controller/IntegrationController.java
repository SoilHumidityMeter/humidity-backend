package com.soilhumidity.backend.controller;

import com.soilhumidity.backend.service.MeasurementService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(MeasurementController.TAG)
public class IntegrationController {

    private final MeasurementService measurementService;

    @GetMapping("/{deviceId}")
    public ResponseEntity<Void> insertMeasurement(
            @PathVariable String deviceId,
            @RequestParam Double humidity,
            @RequestParam String ip
    ) {
        measurementService.insertMeasurement(deviceId, humidity, ip);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
