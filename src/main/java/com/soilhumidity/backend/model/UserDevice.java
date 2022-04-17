package com.soilhumidity.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_devices")
public class UserDevice {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 16)
    private String deviceId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @OneToMany(mappedBy = "userDevice", fetch = FetchType.LAZY)
    private final Set<Measurement> measurements = new HashSet<>();

    @CreationTimestamp
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public UserDevice(String deviceId, User user) {
        this.deviceId = deviceId;
        this.user = user;
    }
}
