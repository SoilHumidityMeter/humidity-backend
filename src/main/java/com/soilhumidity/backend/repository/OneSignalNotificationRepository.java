package com.soilhumidity.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.soilhumidity.backend.model.OneSignalNotification;

@Repository
public interface OneSignalNotificationRepository extends JpaRepository<OneSignalNotification, Long>,
        JpaSpecificationExecutor<OneSignalNotification> {


}
