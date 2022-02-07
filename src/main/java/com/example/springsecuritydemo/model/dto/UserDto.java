package com.example.springsecuritydemo.model.dto;

import com.example.springsecuritydemo.model.*;
import com.example.springsecuritydemo.validation.validation.email.ValidEmail;
import com.example.springsecuritydemo.validation.validation.userType.ValidTypeUser;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {

    Long id;

    @ValidEmail
    @NotBlank(message = "Email may not be blank")
    @NotNull
    @Size(min = 2, max = 45,
            message = "Email must be between 2 and 45 characters long")
    private String email;

    @Size(min = 4, max = 126,
            message = "Password must be between 4 and 32 characters")
    private String password;


    @NotBlank(message = "First name may not be blank")
    @NotNull
    @Size(min = 2, max = 45,
            message = "First name must be between 2 and 45 characters long")
    @Pattern(regexp = "^[a-zA-ZА-Яа-яЇїІі]+$",
            message = "First name must be alphanumeric with no spaces")
    private String firstName;

    @NotBlank(message = "Last name may not be blank")
    @NotNull
    @Size(min = 2, max = 45,
            message = "Last name must be between 2 and 45 characters long")
    @Pattern(regexp = "^[a-zA-ZА-Яа-яЇїІі]+$",
            message = "Last name must be alphanumeric with no spaces")
    private String lastName;


    @Pattern(regexp = "^\\d{10}$", message = "Example = 0967259255")
    @NotBlank
    private String phone;


    private Coach coach;

    @Past
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    // client

    @Min(20)
    @Max(value = 300)
    private Integer height;


    @Digits(integer = 3, fraction = 1)
    @Min(20)
    @Max(300)
    private BigDecimal weight;


    /// coach

    //    @Size(min = 3, max = 1024,
//            message = "Additional info must be between 3 and 1024 characters long")
    @Pattern(regexp = "^[\\w\\s.,\\-'!?+#*А-Яа-яЪъЇїІі]*$",
            message = "Additional info must be alphanumeric with no forbidden characters")
    private String additionalInfo;

    @Pattern(regexp = "^[\\w\\s.,\\-'!?+#*А-Яа-яЪъЇїІі]*$",
            message = "Specialization info must be alphanumeric with no forbidden characters")
    private String specialization;


    //    @Size(min = 10, max = 1024,
//            message = "Education must be between 10 and 1024 characters long")
    @Pattern(regexp = "^[\\w\\s.,\\-'!?+#*А-Яа-яЪъЇїІі]*$",
            message = "Education must be alphanumeric with no forbidden characters")
    private String education;


    //    @Size(min = 10, max = 1024,
//            message = "Achievement must be between 10 and 1024 characters long")
    @Pattern(regexp = "^[\\w\\s.,\\-'!?+#*А-Яа-яЪъЇїІі]*$",
            message = "Achievement must be alphanumeric with no forbidden characters")
    private String achievement;


    private List<Exercise> exercises;
    private List<Client> clients;

    @Column(nullable = true, length = 64)
    private String photos;

    @Enumerated(EnumType.STRING)
    private Role role = Role.CLIENT;
    @Enumerated(EnumType.STRING)
    private StatusCoach statusCoach = StatusCoach.EMPTY;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ValidTypeUser(anyOf = {TypeUser.COACH, TypeUser.CLIENT}, message = "Chose right type of user!")
    private TypeUser typeUser = TypeUser.CLIENT;


    @Transient
    public String getPhotosImagePath() {
        if (photos == null || id == null) return null;

        return "/img/user-photos/" + id + "/" + photos;

    }
}
