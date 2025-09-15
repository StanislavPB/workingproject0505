package org.workinkexceptiondemo.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.workinkexceptiondemo.dto.GeneralResponse;
import org.workinkexceptiondemo.dto.StatusUpdateRequest;
import org.workinkexceptiondemo.dto.TaskRequestDto;
import org.workinkexceptiondemo.dto.TaskResponseDto;
import org.workinkexceptiondemo.entity.Task;
import org.workinkexceptiondemo.entity.TaskStatus;
import org.workinkexceptiondemo.entity.User;
import org.workinkexceptiondemo.repository.TaskRepository;
import org.workinkexceptiondemo.service.exception.NotFoundException;
import org.workinkexceptiondemo.service.util.TaskConverter;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository repository;
    private final UserService service;
    private final TaskConverter converter;


    public TaskResponseDto createTask(TaskRequestDto request) {
/*

- найти нужного user
 */
        Optional<User> taskUserOptional = service.getUserByIdForTaskService(request.getUserId());

        if (taskUserOptional.isEmpty()) {
            throw  new NotFoundException("Невозможно создать задачу для незарегистрированного пользователя. Пользователь с id = " + request.getUserId() + " не найден");
        }

        User taskUser = taskUserOptional.get();

        Task task = converter.fromDto(request);

        task.setUser(taskUser);

        LocalDate today = LocalDate.now();

        task.setCreateDate(today);
        task.setLastUpdate(today);

        task.setStatus(TaskStatus.OPEN);

        Task savedTask = repository.save(task);

        //taskUser.getTasks().add(task); // НЕЗАБЫТЬ !!! что в список задач пользователя необходимо добавить новую задачу

        taskUser.addTask(task); // - то же самое, но с помощью хелпера

        return  converter.toDto(savedTask);

    }


    public TaskResponseDto deleteTaskById(Integer id) {

        Optional<Task> taskByIdOptional = repository.deleteTaskById(id);

        if (taskByIdOptional.isEmpty()) {
            throw new NotFoundException("Задача с id = " + id + " не найдена");
        } else {
            return converter.toDto(taskByIdOptional.get());
        }


    }

    public List<TaskResponseDto> getAllTasksAdmin() {
        List<Task> allTasks = repository.findAll();
        List<TaskResponseDto> response = converter.toDtos(allTasks);
        return response;
    }

    public List<TaskResponseDto> getAllTasksUser(Integer userId) {

        Optional<User> userByIdOptional = service.getUserByIdForTaskService(userId);

        if (userByIdOptional.isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");

        } else {

            /*
            два варианта работы со списком задач конкретного пользователя:

            1) если мы сами НЕ СОХРАНЯЕМ новую задачу в списке задач пользователя, то после добавления новой задачи
            она сохранится в базе данных, но сам автоматически м коллекцию List<Task> у пользователя не появится.

            Если нам нужно будет узнать все задачи конкретного пользователя (актуальный список), то единственный
            вариант - отправить запрос через репозиторий в базу данных "найди все задачи user с id = ..."

            ТО есть для обновления данных о списке задач нам КАЖДЫЙ раз надо будет лазить в БД.

            2) В момент создания новой задачи и записи ее в БД, самим добавить ссылку на объект task в коллекцию
            тасков у пользователя (ручками или через хелпер в класса User (addTask)).
            в этом случае информация об актуальном списке задач пользователя будет одинакова и в БД и в коллекции
            (списке) задач пользователя.
            Тогда при необходимости получения этой информации мы модем не обращаться в репозиторий ("дай нам все
            задачи пользователя с id=..."), а просто у пользователя взять содержимое коллекции List<Task>


             */

            // List<Task> allTasksByUser = repository.findByUser(userByIdOptional.get());

            List<Task> allTasksByUser = userByIdOptional.get().getTasks();

            List<TaskResponseDto> response = converter.toDtos(allTasksByUser);

            return response;
        }
    }


    public TaskResponseDto getTaskById(Integer taskId){
        Optional<Task> taskByIdOptional = repository.findById(taskId);

        if (taskByIdOptional.isEmpty()) {
            throw new NotFoundException("Задача с id = " + taskId + " не найдена");
        } else {
            return converter.toDto(taskByIdOptional.get());
        }

    }

    public List<TaskResponseDto> getTasksWithExpireDeadline() {
        LocalDate today = LocalDate.now();

        List<Task> tasksWithExpireDeadline = repository.findByDeadlineBefore(today);

        List<TaskResponseDto> response = converter.toDtos(tasksWithExpireDeadline);

        return  response;
    }

    public List<TaskResponseDto> getTasksByDateAfter(String date) {
        LocalDate searchDate = LocalDate.parse(date);

        List<Task> tasksWithExpireDeadline = repository.findByDeadlineAfter(searchDate);

        List<TaskResponseDto> response = converter.toDtos(tasksWithExpireDeadline);

        return response;
    }

    public List<TaskResponseDto> getTasksByDateBefore(String date) {
        LocalDate searchDate = LocalDate.parse(date);

        List<Task> tasksWithExpireDeadline = repository.findByDeadlineBefore(searchDate);

        List<TaskResponseDto> response = converter.toDtos(tasksWithExpireDeadline);

        return response;
    }


    public List<TaskResponseDto> getTasksByTaskNameContent(String taskName){

        List<Task> tasksByTaskName = repository.findByTaskNameContainingIgnoreCase(taskName);

        List<TaskResponseDto> response = converter.toDtos(tasksByTaskName);

        return response;
    }

    public String updateTaskStatus(Integer taskId, StatusUpdateRequest request) {
        Optional<Task> taskByIdOptional = repository.findById(taskId);

        if (taskByIdOptional.isEmpty()) {
            throw new NotFoundException("Задача с id = " + taskId + " не найдена");
        } else {
            TaskStatus newStatus = TaskStatus.valueOf(request.getStatus());

            Task task = taskByIdOptional.get();

            task.setStatus(newStatus);

            repository.save(task);

            return "Статус успешно изменен";
        }
    }

}
