package com.soilhumidity.backend.dto.auth;

import com.soilhumidity.backend.dto.generic.MessageDto;

public class LogoutResponse extends MessageDto {
    public LogoutResponse(String message) {
        super(message);
    }
}
