package org.workinkexceptiondemo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.workinkexceptiondemo.annotation.OurValidation;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    @NotBlank
    @Size(min = 3, max = 15)
    private String userName;

    @NotBlank
    @Email
    private String email;

//    @NotBlank
//    @Size(min = 3, max = 15)
//    @Pattern(regexp = "[A-Za-z0-9]+")
    @OurValidation
    private String password;

}
