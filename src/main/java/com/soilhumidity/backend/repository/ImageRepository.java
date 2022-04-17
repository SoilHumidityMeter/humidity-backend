package com.soilhumidity.backend.repository;

import com.soilhumidity.backend.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>, JpaSpecificationExecutor<Image> {

    @Modifying
    @Query(value = "" +
            "insert into images (name,x,y,z) " +
            "values(?1,?2,?3,?4) " +
            "on duplicate key update " +
            "x = ?2,y = ?3,z = ?4", nativeQuery = true)
    void saveOrUpdate(String name, Float x, Float y, Float z);
}
