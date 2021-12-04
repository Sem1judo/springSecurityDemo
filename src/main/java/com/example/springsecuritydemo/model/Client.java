package com.example.springsecuritydemo.model;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "clients")
public class Client extends User {

    @Column
    private Integer height;

    @Column
    private BigDecimal weight;

    @ManyToOne(fetch = FetchType.LAZY)
    private Coach coach;


    @Override
    public String toString() {
        return "Client{" +
                "height=" + height +
                ", weight=" + weight +
                ", coach=" + coach.getFirstName() + " " + coach.getLastName() + ",email =" + coach.getEmail() +
                '}';
    }
}
