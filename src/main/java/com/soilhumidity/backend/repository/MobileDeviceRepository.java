package com.soilhumidity.backend.repository;

import com.soilhumidity.backend.model.MobileDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MobileDeviceRepository extends JpaRepository<MobileDevice, Long> {


    Optional<MobileDevice> deleteByDeviceIdAndUserId(String deviceId, Long userId);

    Optional<MobileDevice> findByDeviceId(String deviceId);

    void deleteAllByUserId(Long userId);
}
