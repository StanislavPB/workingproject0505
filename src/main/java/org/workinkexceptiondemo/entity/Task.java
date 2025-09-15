package org.workinkexceptiondemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String taskName;
    private String taskDescription;
    // RTask - User (ManyToOne)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate createDate;
    private LocalDate lastUpdate;
    private LocalDate deadline;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;
}
