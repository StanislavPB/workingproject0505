package org.workingproject0505.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.workingproject0505.dto.GeneralResponse;
import org.workingproject0505.dto.TaskRequestDto;
import org.workingproject0505.entity.Task;
import org.workingproject0505.entity.User;
import org.workingproject0505.repository.TaskRepository;
import org.workingproject0505.service.util.TaskConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository repository;
    private final TaskConverter converter;



    /*
    createTask
    updateTask
    deleteTask
    getAllTasks
    getTakById
    getTasksWithExpireDeadline
    getTasksByDateAfter
    getTasksByDateBefore
    getTasksByTaskNameContent
    getTasksByUser

     */


    public GeneralResponse<Task> createTask(TaskRequestDto request) {

    }

}
