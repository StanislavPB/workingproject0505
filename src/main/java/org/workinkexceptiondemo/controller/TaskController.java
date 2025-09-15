
package org.workinkexceptiondemo.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.workinkexceptiondemo.dto.GeneralResponse;
import org.workinkexceptiondemo.dto.StatusUpdateRequest;
import org.workinkexceptiondemo.dto.TaskRequestDto;
import org.workinkexceptiondemo.dto.TaskResponseDto;
import org.workinkexceptiondemo.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/create")
    public TaskResponseDto createTask(@RequestBody TaskRequestDto dto) {
        return taskService.createTask(dto);
    }

    @PutMapping("/{taskId}/status")
    public String updateTaskStatus(
            @PathVariable Integer taskId,
            @RequestBody StatusUpdateRequest dto) {
        return taskService.updateTaskStatus(taskId, dto);
    }


    @GetMapping("/user/{userId}")
    public List<TaskResponseDto> getUserTasks(@PathVariable Integer userId) {
        return taskService.getAllTasksUser(userId);
    }

    @GetMapping("/admin")
    public List<TaskResponseDto> getAllTasksAdminMode(){
        return taskService.getAllTasksAdmin();
    }


    @GetMapping("/context")
    public List<TaskResponseDto> getAllTasksByContext(@RequestParam String searchText){
        return taskService.getTasksByTaskNameContent(searchText);
    }


    @GetMapping("/{id}")
    public TaskResponseDto getTaskById(@PathVariable Integer id){
        return taskService.getTaskById(id);
    }

}