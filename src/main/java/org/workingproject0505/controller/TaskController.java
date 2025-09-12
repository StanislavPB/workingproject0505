
package org.workingproject0505.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.workingproject0505.dto.GeneralResponse;
import org.workingproject0505.dto.StatusUpdateRequest;
import org.workingproject0505.dto.TaskRequestDto;
import org.workingproject0505.dto.TaskResponseDto;
import org.workingproject0505.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/create")
    public GeneralResponse<TaskResponseDto> createTask(@RequestBody TaskRequestDto dto) {
        return taskService.createTask(dto);
    }

    @PutMapping("/{taskId}/status")
    public GeneralResponse<String> updateTaskStatus(
            @PathVariable Integer taskId,
            @RequestBody StatusUpdateRequest dto) {
        return taskService.updateTaskStatus(taskId, dto);
    }


    @GetMapping("/user/{userId}")
    public GeneralResponse<List<TaskResponseDto>> getUserTasks(@PathVariable Integer userId) {
        return taskService.getAllTasksUser(userId);
    }

    @GetMapping("/admin")
    public GeneralResponse<List<TaskResponseDto>> getAllTasksAdminMode(){
        return taskService.getAllTasksAdmin();
    }


    @GetMapping("/context")
    public GeneralResponse<List<TaskResponseDto>> getAllTasksByContext(@RequestParam String searchText){
        return taskService.getTasksByTaskNameContent(searchText);
    }

}