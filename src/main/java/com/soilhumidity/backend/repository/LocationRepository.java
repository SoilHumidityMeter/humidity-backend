package com.soilhumidity.backend.repository;

import com.soilhumidity.backend.dto.LocationDto;
import com.soilhumidity.backend.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByName(String name);

    @Query(value = "select new com.soilhumidity.backend.dto.LocationDto(" +
            "l.name," +
            "l.id) " +
            "from Location l " +
            "where (l.parent.id = ?1)")
    List<LocationDto> findAllByParentId(Long parentId);

    Optional<Location> findByNameAndParentName(String name, String parentName);
}
