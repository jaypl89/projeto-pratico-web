package br.edu.ifg.projetopraticoweb.controller;

import br.edu.ifg.projetopraticoweb.dto.ProjectDTO;
import br.edu.ifg.projetopraticoweb.exception.ResourceNotFoundException;
import br.edu.ifg.projetopraticoweb.model.Project;
import br.edu.ifg.projetopraticoweb.model.User;
import br.edu.ifg.projetopraticoweb.service.ProjectService;
import br.edu.ifg.projetopraticoweb.service.UserService;
import br.edu.ifg.projetopraticoweb.mapper.ProjectMapper;
import br.edu.ifg.projetopraticoweb.validator.ProjectDTOValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;
    private final ProjectMapper projectMapper;

    public ProjectController(ProjectService projectService, UserService userService, ProjectMapper projectMapper) {
        this.projectService = projectService;
        this.userService = userService;
        this.projectMapper = projectMapper;
    }

    // Exibe a lista de projetos na página Thymeleaf
    @GetMapping("/list")
    public String listProjects(Model model) {
        User user = userService.getAuthenticatedUser();
        List<ProjectDTO> projects = projectService.findAllByUser(user).stream()
                .map(projectMapper::toDTO)
                .toList();
        model.addAttribute("projects", projects);
        return "project/list";
    }

    @GetMapping("/{id}")
    public String getProjectById(@PathVariable("id") Long id, Model model) {
        Project project = projectService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado com o ID " + id));
        model.addAttribute("project", project);
        return "project/detail";  // Renderiza a página project-detail.html
    }

    // Exibe o formulário de criação de um novo projeto
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("project", new ProjectDTO());
        model.addAttribute("users", userService.findAll());
        return "project/create";
    }

    // Exibe o formulário de edição de um projeto existente
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<Project> project = projectService.findById(id);
        if (project.isPresent()) {
            model.addAttribute("project", projectMapper.toDTO(project.get()));
            model.addAttribute("users", userService.findAll());
            return "project/edit";
        } else {
            return "redirect:/projects";
        }
    }

}
