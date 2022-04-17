package com.soilhumidity.backend.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDeviceDto {

    private String deviceId;

    private Long id;

    private Date createdAt;
}
