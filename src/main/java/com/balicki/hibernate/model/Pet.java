package com.balicki.hibernate.model;

import com.balicki.hibernate.YesNoBooleanConverter;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Trainer> trainerSet;


}
