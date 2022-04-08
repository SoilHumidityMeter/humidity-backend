package com.soilhumidity.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.soilhumidity.backend.model.UserNotification;

@Repository
public interface UserNotificationRepository extends
        JpaRepository<UserNotification, Long>,
        JpaSpecificationExecutor<UserNotification> {

    UserNotification getByUserIdAndOneSignalNotification_OneSignalId(Long userId, String oneSignalId);

}
