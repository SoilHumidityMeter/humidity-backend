package com.soilhumidity.backend.util.service.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

public interface IStorageService {
    URL put(File file);

    URL put(File file, Path path);

    URL put(InputStream inputStream, Path path);

    URL put(InputStream inputStream);

    URL put(MultipartFile file);

    InputStream get(Path path);

    URL getS3Url(String objName);

    void delete(Path path);

    void delete(URL url);

    URL getRemoteUrl(Path path);
}
