package org.workingproject0505.repository;

import org.workingproject0505.entity.Task;
import org.workingproject0505.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Task save(Task task);

    Optional<Task> deleteTaskById(Integer id);

    List<Task> findAll();

    Optional<Task> findById(Integer id);

    List<Task> expiredDeadline();

    List<Task> findByDateAfter(LocalDate date);

    List<Task> findByDateBefore(LocalDate date);

    List<Task> findByTaskNameContent(String taskname);

    List<Task> findByUser(User user);


}
