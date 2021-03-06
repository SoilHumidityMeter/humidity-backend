package com.soilhumidity.backend.dto.auth;

import com.soilhumidity.backend.util.SoilBackendRegex;
import com.soilhumidity.backend.validator.annotation.ClientId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordRequest {
    @ClientId
    @NotBlank(message = "{forgot_password_request.client_id.empty}")
    @ApiModelProperty(required = true, example = "26dc8857-600c-464e-8ec1-062480b01592")
    private String clientId;

    @Email(regexp = SoilBackendRegex.EMAIL, message = "{validation.generic.email.unfit_regex}")
    @ApiModelProperty(required = true, example = "ali.veli@example.com")
    private String email;
}
