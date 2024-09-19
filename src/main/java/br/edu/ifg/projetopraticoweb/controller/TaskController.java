package br.edu.ifg.projetopraticoweb.controller;

import br.edu.ifg.projetopraticoweb.dto.TaskDTO;
import br.edu.ifg.projetopraticoweb.exception.ResourceNotFoundException;
import br.edu.ifg.projetopraticoweb.mapper.TaskMapper;
import br.edu.ifg.projetopraticoweb.model.Task;
import br.edu.ifg.projetopraticoweb.service.AuthenticationService;
import br.edu.ifg.projetopraticoweb.service.TaskService;
import br.edu.ifg.projetopraticoweb.validator.DTOValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final DTOValidator<TaskDTO> validator;
    private final AuthenticationService authenticationService;

    public TaskController(TaskService taskService, TaskMapper taskMapper, AuthenticationService authenticationService) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
        this.authenticationService = authenticationService;
        this.validator = new DTOValidator<>();
    }

    // Listar todas as tarefas
    @GetMapping
    public ResponseEntity<List<TaskDTO>> listTasks() {
        List<TaskDTO> tasks = taskService.findAllByUser(authenticationService.getAuthenticatedUser()).stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    // Obter uma tarefa por ID
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        Task task = taskService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada!"));
        return ResponseEntity.ok(taskMapper.toDTO(task));
    }

    // Criar uma nova tarefa
    @PostMapping("/new")
    public ResponseEntity<String> createTask(@RequestBody TaskDTO taskDTO) {
        if (!validator.isValid(taskDTO)) {
            return ResponseEntity.badRequest().body(validator.validationMessages(taskDTO).toString());
        }

        try {
            Task task = taskMapper.toEntity(taskDTO);
            task.setUser(authenticationService.getAuthenticatedUser());
            taskService.save(task);
            return ResponseEntity.created(URI.create("/tasks/" + task.getId())).build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Atualizar uma tarefa existente
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        Optional<Task> taskOptional = taskService.findById(id);
        if (!authenticationService.authenticate(taskOptional.orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada")
        ).getUser().getEmail())) {
            return ResponseEntity.badRequest().body("Usuário não autorizado");
        }

        if (!validator.isValid(taskDTO)) {
            return ResponseEntity.badRequest().body(validator.validationMessages(taskDTO).toString());
        }

        try {
            Task task = taskMapper.toEntity(taskDTO);
            task.setId(id);
            task.setUser(taskOptional.get().getUser());
            taskService.save(task);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Deletar uma tarefa por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        Optional<Task> taskOptional = taskService.findById(id);
        if (!authenticationService.authenticate(taskOptional.orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada")
        ).getUser().getEmail())) {
            return ResponseEntity.badRequest().body("Usuário não autorizado");
        }

        try {
            taskService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
