package com.example.springsecuritydemo.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "coaches")
public class Coach extends User {

    @Column
    private String additionalInfo;

    @Column
    private String education;

    @Column
    private String achievement;

    @OneToMany(
            mappedBy = "coach",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Client> clients = new ArrayList<>();


    @Override
    public String toString() {
        return "Coach{" +
                "additionalInfo='" + additionalInfo + '\'' +
                ", education='" + education + '\'' +
                ", achievement='" + achievement + '\'' +
                ", clients=" + clients +
                '}';
    }
}
