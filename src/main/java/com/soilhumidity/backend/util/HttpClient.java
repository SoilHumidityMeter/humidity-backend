package com.soilhumidity.backend.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soilhumidity.backend.dto.Response;
import com.soilhumidity.backend.enums.EErrorCode;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


@Component
public class HttpClient {

    private final okhttp3.OkHttpClient client;

    private final ObjectMapper objectMapper;

    public HttpClient() {
        client = new OkHttpClient();
        objectMapper = new ObjectMapper();
    }

    private Response<Object> setUpResponse(okhttp3.Call call) {

        okhttp3.Response response = null;
        try {
            response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return Response.ok(response.body().string());
            } else {
                return Response.notOk("Unsuccessfull", EErrorCode.BAD_REQUEST);
            }
        } catch (IOException e) {
            return Response.notOk(e.getMessage(), EErrorCode.UNHANDLED);
        }

    }

    public Response<Object> post(String endpoint, Object body) {
        try {
            RequestBody requestBody = RequestBody.create(objectMapper.writeValueAsString(body), MediaType.parse("application/json"));

            Request req = new Request.Builder().url(endpoint).post(requestBody).build();

            return setUpResponse(client.newCall(req));

        } catch (JsonProcessingException e) {
            return Response.notOk(e.getMessage(), EErrorCode.UNHANDLED);
        }
    }

    public Response<Object> get(String endpoint, Map<String, Object> queryParams) {

        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(endpoint).newBuilder();

        if (!queryParams.isEmpty()) {
            queryParams.forEach((k, v) -> urlBuilder.addQueryParameter(k, v.toString()));
        }

        Request req = new Request.Builder().url(urlBuilder.build().toString()).build();

        return setUpResponse(client.newCall(req));

    }

    public <T> Response<T> readResult(Response<Object> result, Class<T> clazz) {

        if (result.isOk()) {
            try {
                return Response.ok(objectMapper.readValue(result.getData().toString(), clazz));
            } catch (JsonProcessingException e) {
                return Response.notOk(e.getMessage(), EErrorCode.UNHANDLED);
            }
        }
        return Response.notOk(result.getError());
    }

}
