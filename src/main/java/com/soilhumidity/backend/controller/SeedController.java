package com.soilhumidity.backend.controller;

import com.soilhumidity.backend.dto.Response;
import com.soilhumidity.backend.dto.message.BasicResponse;
import com.soilhumidity.backend.dto.seed.SeedDto;
import com.soilhumidity.backend.dto.seed.SeedRequest;
import com.soilhumidity.backend.enums.ERole;
import com.soilhumidity.backend.service.SeedService;
import com.soilhumidity.backend.util.annotations.ApiInformation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@Api(tags = SeedController.TAG)
@RequestMapping(SeedController.TAG)
@ApiInformation(tag = SeedController.TAG, description = "Seeds related endpoints.")
@AllArgsConstructor
@RolesAllowed(ERole.SYSADMIN)
public class SeedController {

    protected static final String TAG = "seeds";

    private final SeedService seedService;

    @PostMapping
    @ApiOperation(value = "Add a new seed.")
    public ResponseEntity<Response<SeedDto>> addNewSeed(
            @RequestPart("body") SeedRequest body,
            @RequestPart("file") MultipartFile file
    ) {
        return seedService.addSeed(body, file).toResponseEntity();
    }

    @GetMapping
    @ApiOperation(value = "Get seeds.")
    public ResponseEntity<Response<List<SeedDto>>> getSeeds(
    ) {
        return seedService.getSeeds().toResponseEntity();
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Remove seed")
    public ResponseEntity<Response<BasicResponse>> removeSeed(
            @PathVariable Long id
    ) {
        return seedService.removeSeed(id).toResponseEntity();
    }

}
