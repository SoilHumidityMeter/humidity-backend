package com.soilhumidity.backend.factory;

import com.soilhumidity.backend.dto.seed.SeedDto;
import com.soilhumidity.backend.dto.seed.SeedRequest;
import com.soilhumidity.backend.model.Seed;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class SeedFactory {
    public Seed createSeed(SeedRequest body, String url) {
        return new Seed(
                body.getName(),
                body.getDescription(),
                url,
                body.getLink(),
                body.getHumidityUp(),
                body.getHumidityDown()
        );
    }

    public SeedDto createSeedDto(Seed saved) {
        return new SeedDto(
                saved.getName(),
                saved.getDescription(),
                saved.getImage(),
                saved.getLink(),
                saved.getId(),
                saved.getHumidityUp(),
                saved.getHumidityDown()
        );
    }
}
