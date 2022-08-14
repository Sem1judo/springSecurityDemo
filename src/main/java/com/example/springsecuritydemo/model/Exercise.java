package com.example.springsecuritydemo.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String link;

    @Column
    private String quantity;

    @Column
    private String note;

    @ManyToMany(mappedBy = "exercises")
    @ToString.Exclude
    private List<Client> clients;
}
