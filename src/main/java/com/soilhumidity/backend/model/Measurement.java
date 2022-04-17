package com.soilhumidity.backend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@DynamicUpdate
@Table(name = "measurements")
@Getter
@Setter
@NoArgsConstructor
public class Measurement {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double humidity;

    @ManyToOne
    @JoinColumn(name = "user_device_id")
    private UserDevice userDevice;

    @CreationTimestamp
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Measurement(Double humidity, UserDevice userDevice) {
        this.humidity = humidity;
        this.userDevice = userDevice;
    }
    
}
