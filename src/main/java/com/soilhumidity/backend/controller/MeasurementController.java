package com.soilhumidity.backend.controller;

import com.soilhumidity.backend.dto.PageFilter;
import com.soilhumidity.backend.dto.Response;
import com.soilhumidity.backend.dto.measurement.MeasurementDto;
import com.soilhumidity.backend.service.MeasurementService;
import com.soilhumidity.backend.util.annotations.ApiInformation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = MeasurementController.TAG)
@RequestMapping(MeasurementController.TAG)
@ApiInformation(tag = MeasurementController.TAG, description = "Measurements related endpoints.")
@AllArgsConstructor
public class MeasurementController {

    protected static final String TAG = "measurements";

    private final MeasurementService measurementService;

    @GetMapping
    public ResponseEntity<Response<Page<MeasurementDto>>> getMeasurements(
            PageFilter pageFilter,
            @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token) {
        return measurementService.getMeasurements(pageFilter, token).toResponseEntity();
    }

    @GetMapping("map")
    public ResponseEntity<Response<List<MeasurementDto>>> getMeasurementsWhole() {
        return measurementService.getMeasurementsWhole().toResponseEntity();
    }

    @GetMapping("avg/{city}")
    public ResponseEntity<Response<MeasurementDto>> getAvgMeasurements(@PathVariable String city) {
        return measurementService.getAvgMeasurements(city).toResponseEntity();
    }
}
