package org.workingproject0505.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String userName;
    private String email;
    private String password;
    private Role role;
    private LocalDate createDate;
    private LocalDate lastUpdate;
}
