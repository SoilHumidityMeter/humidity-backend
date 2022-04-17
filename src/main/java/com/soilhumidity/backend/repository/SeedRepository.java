package com.soilhumidity.backend.repository;

import com.soilhumidity.backend.model.Seed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeedRepository extends JpaRepository<Seed, Long> {
}
