package com.soilhumidity.backend.service;

import com.soilhumidity.backend.dto.Response;
import com.soilhumidity.backend.dto.message.BasicResponse;
import com.soilhumidity.backend.dto.seed.SeedDto;
import com.soilhumidity.backend.dto.seed.SeedRequest;
import com.soilhumidity.backend.enums.EErrorCode;
import com.soilhumidity.backend.factory.SeedFactory;
import com.soilhumidity.backend.repository.SeedRepository;
import com.soilhumidity.backend.util.service.storage.IStorageService;
import lombok.AllArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SeedService {

    private final SeedRepository seedRepository;
    private final IStorageService storageService;
    private final MessageSourceAccessor messageSource;
    private final SeedFactory seedFactory;


    @Transactional(readOnly = true)
    public Response<List<SeedDto>> getSeeds() {
        return Response.ok(seedRepository.findAll().stream()
                .map(seedFactory::createSeedDto).collect(Collectors.toList()));
    }

    @Transactional
    public Response<SeedDto> addSeed(SeedRequest body, MultipartFile file) {
        try {

            var is = file.getInputStream();

            // Upload image.
            var url = storageService.put(is).toExternalForm();

            var seedFact = seedFactory.createSeed(body, url);

            var saved = seedRepository.save(seedFact);
            return Response.ok(seedFactory.createSeedDto(saved));

        } catch (IOException e) {
            return Response.notOk(messageSource
                    .getMessage("update_profile_picture.io.fail"), EErrorCode.UNHANDLED);
        }
    }

    @Transactional
    public Response<BasicResponse> removeSeed(Long id) {

        seedRepository.deleteById(id);
        return Response.ok(BasicResponse.of("Seed removed successfully"));
    }
}
