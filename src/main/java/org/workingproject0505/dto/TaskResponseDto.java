package org.workingproject0505.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.workingproject0505.entity.TaskStatus;
import org.workingproject0505.entity.User;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDto {
    private Integer id;
    private String taskName;
    private String taskDescription;
    private String userName;
    private String deadline;
    private String status;
}
