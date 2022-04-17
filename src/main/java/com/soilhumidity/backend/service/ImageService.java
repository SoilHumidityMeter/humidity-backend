package com.soilhumidity.backend.service;

import com.soilhumidity.backend.dto.Response;
import com.soilhumidity.backend.dto.image.ImageRequest;
import com.soilhumidity.backend.dto.image.PointZ;
import com.soilhumidity.backend.dto.message.BasicResponse;
import com.soilhumidity.backend.model.Image_;
import com.soilhumidity.backend.repository.ImageRepository;
import com.soilhumidity.backend.util.StringUtil;
import com.soilhumidity.backend.util.service.storage.s3.S3Service;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ImageService {

    private final S3Service s3Service;

    private final ImageRepository imageRepository;

    @Transactional
    public Response<BasicResponse> uploadBatch(List<ImageRequest> body) {


        for (ImageRequest imageRequest : body) {
            imageRepository.saveOrUpdate(
                    imageRequest.getName(),
                    imageRequest.getPoint().getX(),
                    imageRequest.getPoint().getY(),
                    imageRequest.getPoint().getZ());
        }

        return Response.ok(new BasicResponse("Successfully uploaded"));
    }

    @Transactional
    public Response<List<String>> getImages(PointZ point, Float radius) {

        var images =
                imageRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.and(
                                criteriaBuilder.lessThanOrEqualTo(criteriaBuilder.abs(
                                        criteriaBuilder.diff(root.get(Image_.x), point.getX())
                                ), radius),
                                criteriaBuilder.lessThanOrEqualTo(criteriaBuilder.abs(
                                        criteriaBuilder.diff(root.get(Image_.y), point.getY())
                                ), radius),
                                criteriaBuilder.lessThanOrEqualTo(criteriaBuilder.abs(
                                        criteriaBuilder.diff(root.get(Image_.z), point.getZ())
                                ), radius)
                        ));

        var urls = new ArrayList<String>();

        images.forEach(image -> {
            if (StringUtil.isValid(image.getUrl())) {
                urls.add(image.getUrl());
            } else {
                var url = s3Service.getS3Url(image.getName());
                if (url != null) {
                    image.setUrl(url.toExternalForm());
                    urls.add(image.getUrl());
                }
            }
        });

        return Response.ok(urls);
    }
}
