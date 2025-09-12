package org.workingproject0505.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.workingproject0505.dto.GeneralResponse;
import org.workingproject0505.dto.StatusUpdateRequest;
import org.workingproject0505.dto.TaskRequestDto;
import org.workingproject0505.dto.TaskResponseDto;
import org.workingproject0505.entity.Task;
import org.workingproject0505.entity.TaskStatus;
import org.workingproject0505.entity.User;
import org.workingproject0505.repository.TaskRepository;
import org.workingproject0505.service.util.TaskConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository repository;
    private final UserService service;
    private final TaskConverter converter;


    public GeneralResponse<TaskResponseDto> createTask(TaskRequestDto request) {
/*

- найти нужного user
 */
        Optional<User> taskUserOptional = service.getUserByIdForTaskService(request.getUserId());

        if (taskUserOptional.isEmpty()) {
            return new GeneralResponse<>(HttpStatus.NOT_FOUND, null, "Невозможно создать задачу для незарегистрированного пользователя. Пользователь с id = " + request.getUserId() + " не найден");
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

        return new GeneralResponse<>(HttpStatus.CREATED, converter.toDto(savedTask), "Новая задача успешно создана");

    }


    public GeneralResponse<TaskResponseDto> deleteTaskById(Integer id) {

        Optional<Task> taskByIdOptional = repository.deleteTaskById(id);

        if (taskByIdOptional.isEmpty()) {
            return new GeneralResponse<>(HttpStatus.NOT_FOUND, null, "Задача с id = " + id + " не найдена");
        } else {
            return new GeneralResponse<>(HttpStatus.OK, converter.toDto(taskByIdOptional.get()), "Задача с id = " + id + " успешно удалена ");
        }


    }

    public GeneralResponse<List<TaskResponseDto>> getAllTasksAdmin() {
        List<Task> allTasks = repository.findAll();
        List<TaskResponseDto> response = converter.toDtos(allTasks);
        return new GeneralResponse<>(HttpStatus.OK, response, "Список всех задач");
    }

    public GeneralResponse<List<TaskResponseDto>> getAllTasksUser(Integer userId) {

        Optional<User> userByIdOptional = service.getUserByIdForTaskService(userId);

        if (userByIdOptional.isEmpty()) {
            return new GeneralResponse<>(HttpStatus.NOT_FOUND, null, "Пользователь с id = " + userId + " не найден");

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

            return new GeneralResponse<>(HttpStatus.OK, response, "Список всех задач для пользователя с id = " + userId);
        }
    }


    public GeneralResponse<TaskResponseDto> getTaskById(Integer taskId){
        Optional<Task> taskByIdOptional = repository.findById(taskId);

        if (taskByIdOptional.isEmpty()) {
            return new GeneralResponse<>(HttpStatus.NOT_FOUND, null, "Задача с id = " + taskId + " не найдена");
        } else {
            return new GeneralResponse<>(HttpStatus.OK, converter.toDto(taskByIdOptional.get()), "Задача с id = " + taskId);
        }

    }

    public GeneralResponse<List<TaskResponseDto>> getTasksWithExpireDeadline() {
        LocalDate today = LocalDate.now();

        List<Task> tasksWithExpireDeadline = repository.findByDeadlineBefore(today);

        List<TaskResponseDto> response = converter.toDtos(tasksWithExpireDeadline);

        return new GeneralResponse<>(HttpStatus.OK, response, "Список просроченных задач");
    }

    public GeneralResponse<List<TaskResponseDto>> getTasksByDateAfter(String date) {
        LocalDate searchDate = LocalDate.parse(date);

        List<Task> tasksWithExpireDeadline = repository.findByDeadlineAfter(searchDate);

        List<TaskResponseDto> response = converter.toDtos(tasksWithExpireDeadline);

        return new GeneralResponse<>(HttpStatus.OK, response, "Список задач со сроком выполнения после " + date);
    }

    public GeneralResponse<List<TaskResponseDto>> getTasksByDateBefore(String date) {
        LocalDate searchDate = LocalDate.parse(date);

        List<Task> tasksWithExpireDeadline = repository.findByDeadlineBefore(searchDate);

        List<TaskResponseDto> response = converter.toDtos(tasksWithExpireDeadline);

        return new GeneralResponse<>(HttpStatus.OK, response, "Список задач со сроком выполнения до " + date);
    }


    public GeneralResponse<List<TaskResponseDto>> getTasksByTaskNameContent(String taskName){

        List<Task> tasksByTaskName = repository.findByTaskNameContainingIgnoreCase(taskName);

        List<TaskResponseDto> response = converter.toDtos(tasksByTaskName);

        return new GeneralResponse<>(HttpStatus.OK, response, "Список задач названием, содержащим " + taskName);
    }

    public GeneralResponse<String> updateTaskStatus(Integer taskId, StatusUpdateRequest request) {
        Optional<Task> taskByIdOptional = repository.findById(taskId);

        if (taskByIdOptional.isEmpty()) {
            return new GeneralResponse<>(HttpStatus.NOT_FOUND, null, "Задача с id = " + taskId + " не найдена");
        } else {
            TaskStatus newStatus = TaskStatus.valueOf(request.getStatus());

            Task task = taskByIdOptional.get();

            task.setStatus(newStatus);

            repository.save(task);

            return new GeneralResponse<>(HttpStatus.OK, "Статус успешно изменен", "Задача с id = " + taskId);
        }
    }

}
