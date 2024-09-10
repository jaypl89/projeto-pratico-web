package br.edu.ifg.projetopraticoweb.controller;

import br.edu.ifg.projetopraticoweb.model.Task;
import br.edu.ifg.projetopraticoweb.service.ProjectService;
import br.edu.ifg.projetopraticoweb.service.TaskService;
import br.edu.ifg.projetopraticoweb.service.UserService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.List;
import java.util.Optional;

@Controller
@Path("/tasks")
public class TaskController {

    private final TaskService taskService;

    private final ProjectService projectService;

    private final UserService userService;

    public TaskController(TaskService taskService, ProjectService projectService, UserService userService) {
        this.taskService = taskService;
        this.projectService = projectService;
        this.userService = userService;
    }

    // Exibe a lista de tarefas na página Thymeleaf
    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public String listTasks(Model model) {
        List<Task> tasks = taskService.findAll();
        model.addAttribute("tasks", tasks);
        return "task-list";  // Thymeleaf page to render task list (task-list.html)
    }

    // Exibe o formulário de criação de uma nova tarefa
    @GET
    @Path("/create")
    @Produces(MediaType.TEXT_HTML)
    public String showCreateForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("users", userService.findAll());  // Adiciona a lista de usuários
        model.addAttribute("projects", projectService.findAll());  // Adiciona a lista de projetos
        return "task-create";  // Thymeleaf page to render create form (task-create.html)
    }

    // Processa o envio do formulário de criação de tarefa
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String createTask(@ModelAttribute Task task) {
        taskService.save(task);
        return "redirect:/tasks/";
    }

    // Exibe o formulário de edição de uma tarefa existente
    @GET
    @Path("/edit/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String showEditForm(@PathParam("id") Long id, Model model) {
        Optional<Task> task = taskService.findById(id);
        if (task.isPresent()) {
            model.addAttribute("task", task.get());
            model.addAttribute("users", userService.findAll());  // Lista de usuários para o dropdown
            model.addAttribute("projects", projectService.findAll());  // Lista de projetos
            return "task-edit";  // Thymeleaf page to render edit form (task-edit.html)
        } else {
            return "redirect:/tasks/";  // Redireciona se a tarefa não for encontrada
        }
    }

    // Processa o envio do formulário de edição de tarefa
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String updateTask(@ModelAttribute Task task) {
        taskService.save(task);  // Atualiza a tarefa se o ID já existir
        return "redirect:/tasks/";
    }

    // Processa a exclusão de uma tarefa
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String deleteTask(@PathParam("id") Long id) {
        taskService.delete(id);
        return "redirect:/tasks/";
    }
}

