package org.workinkexceptiondemo.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.workinkexceptiondemo.entity.Task;
import org.workinkexceptiondemo.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface TaskRepository extends JpaRepository<Task, Integer> {


    Optional<Task> deleteTaskById(Integer id);


    List<Task> findByDeadlineAfter(LocalDate date);

    List<Task> findByDeadlineBefore(LocalDate date);

    List<Task> findByTaskNameContainingIgnoreCase(String taskname);

    List<Task> findByUser(User user);


}
