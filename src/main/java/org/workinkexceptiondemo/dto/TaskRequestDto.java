package org.workinkexceptiondemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDto {

    private String taskName;
    private String taskDescription;
    private Integer userId;
    private String deadline;
}
