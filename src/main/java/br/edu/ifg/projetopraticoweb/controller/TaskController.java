package br.edu.ifg.projetopraticoweb.controller;

import br.edu.ifg.projetopraticoweb.dto.TaskDTO;
import br.edu.ifg.projetopraticoweb.enum_data.Status;
import br.edu.ifg.projetopraticoweb.exception.ResourceNotFoundException;
import br.edu.ifg.projetopraticoweb.model.Project;
import br.edu.ifg.projetopraticoweb.model.Task;
import br.edu.ifg.projetopraticoweb.model.User;
import br.edu.ifg.projetopraticoweb.service.ProjectService;
import br.edu.ifg.projetopraticoweb.service.TaskService;
import br.edu.ifg.projetopraticoweb.service.UserService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.modelmapper.ModelMapper;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Path("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public TaskController(TaskService taskService, ProjectService projectService, UserService userService, ModelMapper modelMapper) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String listTasks(Model model) {
        User user = getAuthenticatedUser();
        List<TaskDTO> tasks = taskService.findAllByUser(user).stream()
                .map(this::convertToTaskDTO)
                .collect(Collectors.toList());
        model.addAttribute("tasks", tasks);
        return "task-list";
    }

    @GET
    @Path("/create")
    @Produces(MediaType.TEXT_HTML)
    public String showCreateForm(Model model) {
        populateModelForForm(model, new TaskDTO());
        return "task-create";
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response createTask(@BeanParam TaskDTO taskDTO, Model model) {
        return getResponse(taskDTO, model);
    }

    private Response getResponse(@BeanParam TaskDTO taskDTO, Model model) {
        if (!isValid(taskDTO)) {
            populateModelForForm(model, taskDTO);
            return Response.status(Response.Status.BAD_REQUEST).entity("Dados inválidos").build();
        }

        try {
            Task task = convertToTask(taskDTO);
            taskService.save(task);
            return Response.seeOther(URI.create("/tasks/")).build();
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            populateModelForForm(model, taskDTO);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/edit/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String showEditForm(@PathParam("id") Long id, Model model) {
        Optional<Task> task = taskService.findById(id);
        if (task.isPresent()) {
            model.addAttribute("task", convertToTaskDTO(task.get()));
            return "task-edit";
        } else {
            return "redirect:/tasks/";
        }
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response updateTask(@BeanParam TaskDTO taskDTO, Model model) {
        return getResponse(taskDTO, model);
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.TEXT_HTML)
    public Response deleteTask(@PathParam("id") Long id) {
        taskService.delete(id);
        return Response.seeOther(URI.create("/tasks/")).build();
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();  // Email do usuário autenticado
        return userService.findByEmail(currentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
    }

    private Project findProjectById(Long projectId) {
        return projectService.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado."));
    }

    private Task convertToTask(TaskDTO taskDTO) {
        Task task = modelMapper.map(taskDTO, Task.class);
        task.setUser(getAuthenticatedUser());
        task.setProject(findProjectById(taskDTO.getProjectId()));
        task.setDeadline(LocalDate.parse(taskDTO.getDeadline(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        task.setStatus(Status.valueOf(taskDTO.getStatus()));
        return task;
    }

    private TaskDTO convertToTaskDTO(Task task) {
        TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);
        taskDTO.setProjectId(task.getProject().getId());
        taskDTO.setStatus(task.getStatus().getDescription());
        taskDTO.setDeadline(task.getDeadline().toString());
        return taskDTO;
    }

    private void populateModelForForm(Model model, TaskDTO taskDTO) {
        model.addAttribute("task", taskDTO);
    }

    private boolean isValid(TaskDTO taskDTO) {
        // Verificação do título não ser nulo ou vazio
        if (taskDTO.getTitle() == null || taskDTO.getTitle().trim().isEmpty()) {
            return false;
        }

        // Verificação do comprimento da descrição não exceder 2000 caracteres
        if (taskDTO.getDescription() != null && taskDTO.getDescription().length() > 2000) {
            return false;
        }

        // Verificação do formato da data de vencimento
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(taskDTO.getDeadline(), formatter);
        } catch (DateTimeParseException e) {
            return false;
        }

        // Verificação do status ser um dos valores permitidos usando o enum
        try {
            Status.fromDescription(taskDTO.getStatus());
        } catch (IllegalArgumentException e) {
            return false;
        }

        // Verificação do projectId ser positivo
        if (taskDTO.getProjectId() == null || taskDTO.getProjectId() <= 0) {
            return false;
        }

        // Se todas as verificações passaram, o DTO é considerado válido
        return true;
    }

}
