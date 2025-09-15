package org.workinkexceptiondemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String userName;
    private String email;
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
