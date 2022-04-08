package com.soilhumidity.backend.dto.auth;

import com.soilhumidity.backend.enums.EGrantType;
import com.soilhumidity.backend.validator.annotation.ClientId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequest {
    @NotNull(message = "{login_request.grant_type.empty}")
    @ApiModelProperty(required = true, example = "password")
    private EGrantType grant_type;

    @ClientId
    @NotBlank(message = "{login_request.client_id.empty}")
    @ApiModelProperty(required = true, example = "26dc8857-600c-464e-8ec1-062480b01592")
    private String client_id;

    @ApiModelProperty(example = "26dc8857-600c-464e-8ec1-062480b01592")
    private String client_secret;

    @ApiModelProperty(example = "ali.veli@example.com")
    @Getter
    private String username;

    @ApiModelProperty(example = "p4ssw0rd")
    private String password;
}
