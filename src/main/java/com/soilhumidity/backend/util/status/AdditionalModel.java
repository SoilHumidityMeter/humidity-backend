package com.soilhumidity.backend.util.status;

import com.soilhumidity.backend.dto.Response;

public class AdditionalModel {
    public static final Class<?> MODEL = Response.class;
    public static final String NAME = MODEL.getSimpleName();
    public static final String PATH = MODEL.getName().split("." + NAME)[0];
}
