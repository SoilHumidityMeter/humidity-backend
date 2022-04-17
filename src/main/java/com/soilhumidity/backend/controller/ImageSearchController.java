package com.soilhumidity.backend.controller;

import com.soilhumidity.backend.dto.Response;
import com.soilhumidity.backend.dto.image.ImageRequest;
import com.soilhumidity.backend.dto.image.PointZ;
import com.soilhumidity.backend.dto.message.BasicResponse;
import com.soilhumidity.backend.enums.ERole;
import com.soilhumidity.backend.service.ImageService;
import com.soilhumidity.backend.util.annotations.ApiInformation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@AllArgsConstructor
@Api(tags = ImageSearchController.TAG)
@ApiInformation(tag = ImageSearchController.TAG, description = "Searches and finds images by given keywords")
@RequestMapping(ImageSearchController.TAG)
public class ImageSearchController {

    protected static final String TAG = "images";

    private final ImageService imageService;

    @GetMapping
    @ApiOperation(value = "Get images by radius")
    public ResponseEntity<Response<List<String>>> getImages(
            @RequestParam Float x,
            @RequestParam Float y,
            @RequestParam Float z,
            @RequestParam(defaultValue = "5.0", required = false) Float radius) {
        return imageService.getImages(PointZ.of(x, y, z), radius).toResponseEntity();
    }

    @PostMapping("batch")
    @ApiOperation(value = "Upload images as batch")
    @RolesAllowed(ERole.SYSADMIN)
    public ResponseEntity<Response<BasicResponse>> uploadBatch(@RequestBody List<ImageRequest> body) {

        return imageService.uploadBatch(body).toResponseEntity();
    }
}
