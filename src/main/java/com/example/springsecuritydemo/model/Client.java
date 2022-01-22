package com.example.springsecuritydemo.model;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "clients")
@ToString
public class Client extends User {

    @Column
    private Integer height;

    @Column
    private BigDecimal weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Coach coach;

    @Column(name = "status_coach")
    @Enumerated(value = EnumType.STRING)
    private StatusCoach statusCoach;

    @OneToMany(mappedBy="client")
    @ToString.Exclude
    private List<Exercise> exercises;


}
