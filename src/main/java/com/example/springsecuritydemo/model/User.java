package com.example.springsecuritydemo.model;

import com.example.springsecuritydemo.validation.validation.email.ValidEmail;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @ValidEmail
    @NotBlank(message = "Email may not be blank")
    @Size(min = 2, max = 45,
            message = "Email must be between 2 and 45 characters long")
    private String email;

    @Column
    @NotBlank(message = "Password may not be blank")
    @Size(min = 4, max = 126,
            message = "Password must be between 4 and 32 characters")
    private String password;

    @Column
    @NotBlank(message = "First name may not be blank")
    @Size(min = 2, max = 45,
            message = "First name must be between 2 and 45 characters long")
    @Pattern(regexp = "^[a-zA-ZА-Яа-яЇї]+$",
            message = "First name must be alphanumeric with no spaces")
    private String firstName;

    @Column
    @NotBlank(message = "Last name may not be blank")
    @Size(min = 2, max = 45,
            message = "Last name must be between 2 and 45 characters long")
    @Pattern(regexp = "^[a-zA-ZА-Яа-яЇї]+$",
            message = "Last name must be alphanumeric with no spaces")
    private String lastName;

    @Column
    @Pattern(regexp = "^\\d{10}$", message = "Example = 0967259255")
    private String phone;

    @Column(columnDefinition = "DATE")
    @Past
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private Role role;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
