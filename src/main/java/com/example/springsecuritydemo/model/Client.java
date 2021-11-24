package com.example.springsecuritydemo.model;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class Client extends User{

    @Column
    @NotNull
    @Min(20)
    @Max(value = 300)
    private Integer height;

    @Column
    @NotNull
    @Digits(integer = 3, fraction = 1)
    @Min(20)
    @Max(300)
    private BigDecimal weight;

    @ManyToOne(fetch = FetchType.LAZY)
    private Coach coach;

}
