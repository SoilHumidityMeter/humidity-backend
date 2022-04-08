package com.soilhumidity.backend.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasicUserDto {

    private Long id;

    private String name;

    private String surname;

    private String email;

    private String phoneNo;

}
