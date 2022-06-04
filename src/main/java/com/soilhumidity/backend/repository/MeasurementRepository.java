package com.soilhumidity.backend.repository;

import com.soilhumidity.backend.model.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long>, JpaSpecificationExecutor<Measurement> {

    @Query(value = "select AVG(humidity) from measurements where id>0;", nativeQuery = true)
    Double getAverageHumidity();

}
