package com.soilhumidity.backend.controller;

import com.soilhumidity.backend.dto.Response;
import com.soilhumidity.backend.enums.ELocale;
import com.soilhumidity.backend.util.annotations.ApiInformation;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = EnumController.TAG)
@RequestMapping(EnumController.TAG)
@ApiInformation(tag = EnumController.TAG, description = "Enums related endpoints")
@AllArgsConstructor
public class EnumController {

    protected static final String TAG = "enums";
    

    @GetMapping("locales")
    public ResponseEntity<Response<ELocale[]>> getLocales() {
        return Response.ok(ELocale.values()).toResponseEntity();
    }
}
