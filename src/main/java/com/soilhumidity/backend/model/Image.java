package com.soilhumidity.backend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@DynamicUpdate
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private Double x;

    @Column
    private Double y;

    @Column
    private Double z;

    @Column
    private String url;

    public Image(String name, Double x, Double y, Double z) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
