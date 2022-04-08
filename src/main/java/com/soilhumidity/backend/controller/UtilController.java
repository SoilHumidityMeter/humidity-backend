package com.soilhumidity.backend.controller;

import com.soilhumidity.backend.dto.Response;
import com.soilhumidity.backend.dto.util.DateTime;
import com.soilhumidity.backend.util.annotations.ApiInformation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.soilhumidity.backend.service.UtilService;

@RestController
@AllArgsConstructor
@Api(tags = UtilController.TAG)
@ApiInformation(tag = UtilController.TAG, description = "Provides many utilities for our developers")
@RequestMapping(UtilController.TAG)
public class UtilController {

    protected static final String TAG = "utils";

    private final UtilService utilService;

    @GetMapping("time")
    @ApiOperation(value = "Get server time")
    public ResponseEntity<Response<DateTime>> getServerTime() {
        return utilService.getServerTime().toResponseEntity();
    }
}
