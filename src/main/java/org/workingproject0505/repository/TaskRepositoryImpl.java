package org.workingproject0505.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.workingproject0505.entity.Task;
import org.workingproject0505.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepositoryImpl implements TaskRepository{

    private final List<Task> tasks;
    private Integer taskIdCounter;

    public TaskRepositoryImpl() {
        this.tasks = new ArrayList<>();
        this.taskIdCounter = 0;
    }

    @Override
    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(++taskIdCounter);
            tasks.add(task);
        } else {
            tasks.removeIf(t -> t.getId().equals(task.getId()));
            tasks.add(task);
        }
        return task;
    }


    @Override
    public Optional<Task> deleteTaskById(Integer id) {
        Optional<Task> taskForDeleteOptional = findById(id);
        if (taskForDeleteOptional.isEmpty()) {
            return Optional.empty();
        } else {
            tasks.remove(taskForDeleteOptional.get());
            return taskForDeleteOptional;
        }
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks);
    }

    @Override
    public Optional<Task> findById(Integer id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();
    }


    @Override
    public List<Task> findByDateAfter(LocalDate date) {
        return tasks.stream()
                .filter(task -> task.getDeadline().isAfter(date))
                .toList();
    }

    @Override
    public List<Task> findByDateBefore(LocalDate date) {
        return tasks.stream()
                .filter(task -> task.getDeadline().isBefore(date))
                .toList();
    }

    @Override
    public List<Task> findByTaskNameContent(String taskname) {
        return tasks.stream()
                .filter(task -> task.getTaskName().toLowerCase().contains(taskname.toLowerCase()))
                .toList();
    }

    @Override
    public List<Task> findByUser(User user) {
        return tasks.stream()
                .filter(task -> task.getUser().equals(user))
                .toList();
    }

}
