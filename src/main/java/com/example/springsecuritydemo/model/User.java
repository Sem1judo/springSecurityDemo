package com.example.springsecuritydemo.model;

import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String phone;

    @Column(columnDefinition = "DATE")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;


    @Enumerated(EnumType.STRING)
    @Column
    private Gender gender;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    @Builder.Default
    private Role role = Role.USER;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private Status status = Status.BANNED;

    @Column
    private boolean enabled;

    @Column
    @Builder.Default
    private LocalDateTime registrationDate = LocalDateTime.now();
}
