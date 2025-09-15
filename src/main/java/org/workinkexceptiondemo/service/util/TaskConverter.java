package org.workinkexceptiondemo.service.util;

import org.springframework.stereotype.Component;
import org.workinkexceptiondemo.dto.TaskRequestDto;
import org.workinkexceptiondemo.dto.TaskResponseDto;
import org.workinkexceptiondemo.entity.Task;


import java.time.LocalDate;
import java.util.List;

@Component
public class TaskConverter {

    public Task fromDto(TaskRequestDto dto) {
        Task task = new Task();

        task.setTaskName(dto.getTaskName());
        task.setTaskDescription(dto.getTaskDescription());
        // по умолчанию используем формат yyyy-mm-dd
        task.setDeadline(LocalDate.parse(dto.getDeadline()));

        /*
        пример своего формата даты

        String dateStr = "04.09.2025";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.parse(dateStr, formatter);

         */

        return task;
    }

    public TaskResponseDto toDto(Task task){

        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setTaskName(task.getTaskName());
        dto.setTaskDescription(task.getTaskDescription());
        dto.setUserName(task.getUser().getUserName());
        dto.setDeadline(task.getDeadline().toString());
        dto.setStatus(task.getStatus().name());

        return dto;

    }

    public List<TaskResponseDto> toDtos(List<Task> tasks) {
        return tasks.stream()
                .map(task -> toDto(task))
                .toList();
    }
}
