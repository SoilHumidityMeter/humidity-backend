package com.soilhumidity.backend.controller;

import com.soilhumidity.backend.util.annotations.ApiInformation;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = MeasurementController.TAG)
@RequestMapping(MeasurementController.TAG)
@ApiInformation(tag = MeasurementController.TAG, description = "Measurements related endpoints.")
@AllArgsConstructor
public class MeasurementController {

    protected static final String TAG = "measurements";
}
