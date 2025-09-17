package org.workinkexceptiondemo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.workinkexceptiondemo.annotation.OurValidation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "User name must be not blank")
    private String userName;

    @NotBlank
    @Email
    private String email;

//    @NotBlank
//    @Size(min = 3, max = 15, message = "User name length must be between from 3 to 15 characters")
//    @Pattern(regexp = "[A-Za-z0-9]+", message = "User password can contain latin characters or digital only")
    @OurValidation(message = "Пароль не соответствует критериям безопасности")
    private String password;

    // одна роль у пользователя
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private LocalDate createDate;
    private LocalDate lastUpdate;

    // определить связь User - Task
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();


    // хелпер для двусторонней связи
    public void addTask(Task task){
        task.setUser(this);
        tasks.add(task);
    }

    public void removeTask(Task task){
        task.setUser(null);
        tasks.remove(task);
    }


}
