package br.edu.ifg.projetopraticoweb.controller;

import br.edu.ifg.projetopraticoweb.model.Project;
import br.edu.ifg.projetopraticoweb.service.ProjectService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@Path("/")
public class HomeController {

    private final ProjectService projectService;

    public HomeController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // Mapeia a página inicial (Home)
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String home(Model model) {
        List<Project> projects = projectService.findAll();  // Busca todos os projetos com suas tarefas
        model.addAttribute("projects", projects);
        return "index";
    }

    // Mapeia a página de login
    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    public String login() {
        return "authentication/login";  // Renderiza a página authentication/login.html
    }
}

