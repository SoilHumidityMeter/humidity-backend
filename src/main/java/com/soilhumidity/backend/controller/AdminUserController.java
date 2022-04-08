package com.soilhumidity.backend.controller;

import com.soilhumidity.backend.dto.PageFilter;
import com.soilhumidity.backend.dto.Response;
import com.soilhumidity.backend.dto.generic.UserCreateRequest;
import com.soilhumidity.backend.dto.message.BasicResponse;
import com.soilhumidity.backend.dto.user.UserDto;
import com.soilhumidity.backend.dto.user.UserUpdateRequest;
import com.soilhumidity.backend.enums.ERole;
import com.soilhumidity.backend.util.annotations.ApiInformation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.soilhumidity.backend.service.AdminUserService;
import com.soilhumidity.backend.specs.UserSpecs;
import com.soilhumidity.backend.specs.annotations.UserFilterParams;

import javax.annotation.security.RolesAllowed;

@RestController
@Api(tags = AdminUserController.TAG)
@RequestMapping(UserController.TAG)
@ApiInformation(tag = AdminUserController.TAG, description = "Admin users related endpoints.")
@AllArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    protected static final String TAG = "admin-users";

    @GetMapping
    @ApiOperation(value = "Get users")
    @RolesAllowed({ERole.ADMIN, ERole.SYSADMIN})
    @UserFilterParams
    public ResponseEntity<Response<Page<UserDto>>> getUsers(
            UserSpecs spec,
            PageFilter pageFilter) {
        return adminUserService.getUsers(spec, pageFilter).toResponseEntity();
    }

    @PostMapping
    @ApiOperation(value = "Create a admin user")
    @RolesAllowed({ERole.ADMIN, ERole.SYSADMIN})
    public ResponseEntity<Response<UserDto>> createUser(
            @RequestBody UserCreateRequest body) {
        return adminUserService.createUser(body).toResponseEntity();
    }

    @PatchMapping("{id}")
    @ApiOperation(value = "Update a user")
    @RolesAllowed({ERole.ADMIN, ERole.SYSADMIN})
    public ResponseEntity<Response<UserDto>> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest body) {
        return adminUserService.updateUser(id, body).toResponseEntity();
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Delete a user")
    @RolesAllowed({ERole.ADMIN, ERole.SYSADMIN})
    public ResponseEntity<Response<BasicResponse>> deleteUser(
            @PathVariable Long id) {
        return adminUserService.deleteUser(id).toResponseEntity();
    }
}
