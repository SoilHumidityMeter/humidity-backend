package com.soilhumidity.backend.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BasicResponse {
    private String content;

    public static BasicResponse of(String content) {
        return new BasicResponse(content);
    }
}
