package br.edu.ifg.projetopraticoweb.controller.api;

import br.edu.ifg.projetopraticoweb.dto.TaskDTO;
import br.edu.ifg.projetopraticoweb.exception.ResourceNotFoundException;
import br.edu.ifg.projetopraticoweb.mapper.TaskMapper;
import br.edu.ifg.projetopraticoweb.model.Task;
import br.edu.ifg.projetopraticoweb.service.TaskService;
import br.edu.ifg.projetopraticoweb.validator.TaskDTOValidator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final TaskDTOValidator validator;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
        this.validator = new TaskDTOValidator();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listTasks() {
        List<TaskDTO> tasks = taskService.findAll().stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
        return Response.ok(tasks).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTaskById(@PathParam("id") Long id) {
        Task task = taskService.findById(id).orElseThrow(() -> new WebApplicationException("Task not found", 404));
        return Response.ok(taskMapper.toDTO(task)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTask(TaskDTO taskDTO) {
        if (!validator.isValid(taskDTO)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Dados inválidos").build();
        }

        try {
            Task task = taskMapper.toEntity(taskDTO);
            taskService.save(task);
            return Response.created(URI.create("/api/tasks/" + task.getId())).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTask(@PathParam("id") Long id, TaskDTO taskDTO) {
        if (!validator.isValid(taskDTO)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Dados inválidos").build();
        }

        try {
            Task task = taskMapper.toEntity(taskDTO);
            task.setId(id);
            taskService.save(task);
            return Response.ok().build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTask(@PathParam("id") Long id) {
        try {
            taskService.delete(id);
            return Response.noContent().build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

}

