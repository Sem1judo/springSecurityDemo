package com.example.springsecuritydemo.model;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank(message = "Name of package may not be blank")
    @Size(min = 3, max = 255,
            message = "Name of package must be between 3 and 255 characters")
    @Pattern(regexp = "^[\\w\\s.,\\-'!?+#*А-Яа-яЪъЇї]*$",
            message = "Name of package must not have forbidden characters ")
    private String name;

    @Column
    @NotBlank
    @Size(min = 3, max = 1024,
            message = "Description must be between 3 and 1024 characters long")
    private String description;

    @Column
    @NotNull
    @Digits(integer = 6, fraction = 2)
    private BigDecimal price;


}
