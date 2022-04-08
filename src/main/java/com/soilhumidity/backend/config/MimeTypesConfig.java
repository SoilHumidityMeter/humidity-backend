package com.soilhumidity.backend.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MimeTypesConfig {

    @Getter
    private final List<String> validImageMimeTypes = List.of("image/jpeg", "image/png", "image/webp", "image/heif", "image/heic");

    @Getter
    private final List<String> validAudioMimeTypes = List.of("audio/basic", "audio/mid", " auido/L24", "audio/mpeg", "audio/mp4", "audio/x-aiff", "");

    @Getter
    private final List<String> validDocumentMimeTypes = List.of("text/csv", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.oasis.opendocument.spreadsheet", "application/pdf", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    @Getter
    private final List<String> validVideoMimeTypes = List.of("video/mpeg", "video/mp4", "video/x-sgi-movie", "video/x-msvideo", "video/webm", "video/H265", "video/H264", "video/H263", "video/H261",
            "video/H263-1998", "video/H263-2000", "video/H264-RCDO", "video/H264-SVC", "video/m4v", "video/x-m4v", "video/quicktime");
}
