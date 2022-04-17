package com.soilhumidity.backend.repository;

import com.soilhumidity.backend.model.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

}
