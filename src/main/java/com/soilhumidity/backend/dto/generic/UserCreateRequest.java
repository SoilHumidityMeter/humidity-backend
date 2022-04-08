package com.soilhumidity.backend.dto.generic;

import com.soilhumidity.backend.util.SoilBackendRegex;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
    @ApiModelProperty(example = "Ali")
    private String name;

    @ApiModelProperty(example = "Veli")
    private String surname;

    @Email(regexp = SoilBackendRegex.EMAIL, message = "{validation.generic.email.unfit_regex}")
    @ApiModelProperty(required = true, example = "ali.veli@example.com")
    private String email;

    @ApiModelProperty(example = "5079696533")
    @Pattern(regexp = SoilBackendRegex.PHONE, message = "{validation.generic.phone.unfit_regex}")
    private String phone;
}
