package com.soilhumidity.backend.repository;

import com.soilhumidity.backend.model.Seed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeedRepository extends JpaRepository<Seed, Long> {
    
    List<Seed> getAllByHumidityDownGreaterThanEqualAndHumidityUpLessThanEqual(Double down, Double up);

}
