package com.soilhumidity.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seeds")
public class Seed {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String image;

    @Column
    private String link;

    @Column
    private Double humidityUp;

    @Column
    private Double humidityDown;

    public Seed(String name, String description, String image, String link, Double humidityUp, Double humidityDown) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.link = link;
        this.humidityUp = humidityUp;
        this.humidityDown = humidityDown;
    }
}
