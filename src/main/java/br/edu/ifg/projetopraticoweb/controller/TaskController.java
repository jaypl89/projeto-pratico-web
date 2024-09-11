package br.edu.ifg.projetopraticoweb.controller;

import br.edu.ifg.projetopraticoweb.dto.TaskDTO;
import br.edu.ifg.projetopraticoweb.exception.ResourceNotFoundException;
import br.edu.ifg.projetopraticoweb.model.Task;
import br.edu.ifg.projetopraticoweb.model.User;
import br.edu.ifg.projetopraticoweb.service.TaskService;
import br.edu.ifg.projetopraticoweb.service.UserService;
import br.edu.ifg.projetopraticoweb.mapper.TaskMapper;
import br.edu.ifg.projetopraticoweb.validator.TaskDTOValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, UserService userService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.userService = userService;
        this.taskMapper = taskMapper;
    }

    // Exibe a lista de tarefas na página Thymeleaf
    @GetMapping("/list")
    public String listTasks(Model model) {
        User user = userService.getAuthenticatedUser();
        List<TaskDTO> tasks = taskService.findAllByUser(user).stream()
                .map(taskMapper::toDTO)
                .toList();
        model.addAttribute("tasks", tasks);
        return "task/list";
    }

    @GetMapping("/{id}")
    public String getTaskById(@PathVariable("id") Long id, Model model) {
        Task task = taskService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com o ID " + id));
        model.addAttribute("task", task);
        return "task/detail";  // Renderiza a página task-detail.html
    }

    // Exibe o formulário de criação de tarefa
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new TaskDTO());
        return "task/create";
    }

    // Exibe o formulário de edição de tarefa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<Task> task = taskService.findById(id);
        if (task.isPresent()) {
            model.addAttribute("task", taskMapper.toDTO(task.get()));
            return "task/edit";
        } else {
            return "redirect:/tasks";
        }
    }

}
