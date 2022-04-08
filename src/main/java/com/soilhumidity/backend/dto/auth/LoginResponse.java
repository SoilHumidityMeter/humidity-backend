package com.soilhumidity.backend.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.soilhumidity.backend.enums.EErrorCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    public final String accessToken;

    public final EErrorCode code;
    public final String message;

    @JsonIgnore
    private final boolean success;

    private LoginResponse(String accessToken, boolean success) {
        this.accessToken = accessToken;

        message = null;
        code = null;
        this.success = success;
    }

    private LoginResponse(String message, EErrorCode code) {
        this.message = message;
        this.code = code;

        accessToken = null;
        success = false;
    }

    public static LoginResponse ok(String accessToken) {
        return new LoginResponse(accessToken, true);
    }

    public static LoginResponse notOk(String message, EErrorCode code) {
        return new LoginResponse(message, code);
    }

    public boolean isSuccess() {
        return success;
    }
}
