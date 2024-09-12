package br.edu.ifg.projetopraticoweb.mapper;

import br.edu.ifg.projetopraticoweb.dto.TaskDTO;
import br.edu.ifg.projetopraticoweb.enum_data.Status;
import br.edu.ifg.projetopraticoweb.model.Project;
import br.edu.ifg.projetopraticoweb.model.Task;
import br.edu.ifg.projetopraticoweb.service.ProjectService;
import br.edu.ifg.projetopraticoweb.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class TaskMapper implements Mapper<Task, TaskDTO>{

    private final ModelMapper modelMapper;
    private final ProjectService projectService;
    private final UserService userService;

    public TaskMapper(ModelMapper modelMapper, ProjectService projectService, UserService userService) {
        this.modelMapper = modelMapper;
        this.projectService = projectService;
        this.userService = userService;
    }

    @Override
    public TaskDTO toDTO(Task task) {
        TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);
        taskDTO.setProjectId(task.getProject().getId());
        taskDTO.setStatus(task.getStatus().getDescription());
        taskDTO.setDeadline(task.getDeadline().toString());
        return taskDTO;
    }

    @Override
    public Task toEntity(TaskDTO taskDTO) {
        Task task = modelMapper.map(taskDTO, Task.class);
        //task.setUser(userService.getAuthenticatedUser());
        Project project = projectService.findById(taskDTO.getProjectId()).orElse(null);
        task.setProject(project);
        task.setDeadline(LocalDate.parse(taskDTO.getDeadline(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        task.setStatus(Status.valueOf(taskDTO.getStatus()));
        return task;
    }
}
