package com.soilhumidity.backend.dto.auth;

import com.soilhumidity.backend.util.SoilBackendRegex;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RegisterRequest {

    @ApiModelProperty(example = "Ali")
    @Size(min = 1, max = 255, message = "{register_request.name.empty}")
    private String name;

    @ApiModelProperty(example = "Veli")
    @Size(min = 1, max = 255, message = "{register_request.surname.empty}")
    private String surname;

    @Email(regexp = SoilBackendRegex.EMAIL, message = "{validation.generic.email.unfit_regex}")
    @ApiModelProperty(required = true, example = "ali.veli@example.com")
    private String email;

    @Getter
    @ApiModelProperty(required = true, example = "p4ssw0rd123")
    @Size(min = 4, max = 32, message = "{register_request.password.invalid}")
    private String password;

    @ApiModelProperty(example = "5079696533")
    @Pattern(regexp = SoilBackendRegex.PHONE, message = "{validation.generic.phone.unfit_regex}")
    private String phone;
}
