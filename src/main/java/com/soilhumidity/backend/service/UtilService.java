package com.soilhumidity.backend.service;

import com.soilhumidity.backend.dto.Response;
import com.soilhumidity.backend.dto.util.DateTime;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UtilService {

    public Response<DateTime> getServerTime() {
        return Response.ok(new DateTime(new Date()));
    }
}
