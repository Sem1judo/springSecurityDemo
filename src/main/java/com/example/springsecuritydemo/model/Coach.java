package com.example.springsecuritydemo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
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
    @NotBlank
    @Size(min = 3, max = 1024,
            message = "Additional info must be between 3 and 1024 characters long")
    @Pattern(regexp = "^[\\w\\s.,\\-'!?+#*А-Яа-яЪъЇї]*$",
            message = "Additional info must be alphanumeric with no forbidden characters")
    private String additionalInfo;

    @Column
    @NotBlank(message = "Education may not be blank")
    @Size(min = 10, max = 1024,
            message = "Education must be between 10 and 1024 characters long")
    @Pattern(regexp = "^[\\w\\s.,\\-'!?+#*А-Яа-яЪъЇї]*$",
            message = "Education must be alphanumeric with no forbidden characters")
    private String education;

    @Column
    @NotBlank(message = "Achievement may not be blank")
    @Size(min = 10, max = 1024,
            message = "Achievement must be between 10 and 1024 characters long")
    @Pattern(regexp = "^[\\w\\s.,\\-'!?+#*А-Яа-яЪъЇї]*$",
            message = "Achievement must be alphanumeric with no forbidden characters")
    private String achievement;

    @OneToMany(
            mappedBy = "coach",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Client> clients = new ArrayList<>();


}
