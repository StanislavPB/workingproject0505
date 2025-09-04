package org.workingproject0505.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    private Integer id;
    private String taskName;
    private String taskDescription;
    private User user;
    private LocalDate createDate;
    private LocalDate lastUpdate;
    private LocalDate deadline;
    private TaskStatus status;
}
