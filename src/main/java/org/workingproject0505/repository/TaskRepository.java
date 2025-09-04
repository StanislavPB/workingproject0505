package org.workingproject0505.repository;

import org.workingproject0505.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Task save(Task task);

    Task updateTask(Task task);

    Task deleteTaskById(Integer id);

    List<Task> findAll();

    Optional<Task> findById(Integer id);

    Optional<Task> changeStatus(Task task);

}
