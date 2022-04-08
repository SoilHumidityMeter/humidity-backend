package com.soilhumidity.backend.dto.log;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soilhumidity.backend.enums.ELogLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LogDto {

    private ELogLevel level;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date date;

    private String message;
}
