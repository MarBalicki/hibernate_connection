package com.balicki.hibernate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Pets")
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int age;
    @Column(name = "owner_name")
    private String ownerName;
    private double weight;

    @Column(name = "pure_race")
    @Convert(converter = YesNoBooleanConverter.class)
    private boolean pureRace;

    @Enumerated(value = EnumType.STRING)
    private Race race;
}
