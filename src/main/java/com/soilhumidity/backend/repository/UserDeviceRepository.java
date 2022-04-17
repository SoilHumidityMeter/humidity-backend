package com.soilhumidity.backend.repository;

import com.soilhumidity.backend.model.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {

    Optional<UserDevice> findByDeviceId(String deviceId);

    boolean existsByDeviceId(String deviceId);
}
