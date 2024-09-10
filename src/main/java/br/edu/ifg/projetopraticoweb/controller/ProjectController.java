package br.edu.ifg.projetopraticoweb.controller;

import br.edu.ifg.projetopraticoweb.model.Project;
import br.edu.ifg.projetopraticoweb.service.ProjectService;
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
@Path("/projects")
public class ProjectController {

    private final ProjectService projectService;

    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    // Exibe a lista de projetos na página Thymeleaf
    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public String listProjects(Model model) {
        List<Project> projects = projectService.findAll();
        model.addAttribute("projects", projects);
        return "project-list";  // Página Thymeleaf para listar projetos (project-list.html)
    }

    // Exibe o formulário de criação de um novo projeto
    @GET
    @Path("/create")
    @Produces(MediaType.TEXT_HTML)
    public String showCreateForm(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("users", userService.findAll());  // Lista de usuários para atribuir como supervisor ou participantes
        return "project-create";  // Página Thymeleaf para criar projeto (project-create.html)
    }

    // Processa o envio do formulário de criação de projeto
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String createProject(@ModelAttribute Project project) {
        projectService.save(project);
        return "redirect:/projects/";  // Redireciona para a lista de projetos
    }

    // Exibe o formulário de edição de um projeto existente
    @GET
    @Path("/edit/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String showEditForm(@PathParam("id") Long id, Model model) {
        Optional<Project> project = projectService.findById(id);
        if (project.isPresent()) {
            model.addAttribute("project", project.get());
            model.addAttribute("users", userService.findAll());  // Carrega a lista de usuários
            return "project-edit";  // Página Thymeleaf para editar projeto (project-edit.html)
        } else {
            return "redirect:/projects/";  // Redireciona se o projeto não for encontrado
        }
    }

    // Processa o envio do formulário de edição de projeto
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String updateProject(@ModelAttribute Project project) {
        projectService.save(project);  // Atualiza o projeto se o ID já existir
        return "redirect:/projects/";
    }

    // Processa a exclusão de um projeto
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String deleteProject(@PathParam("id") Long id) {
        projectService.delete(id);
        return "redirect:/projects/";
    }
}

