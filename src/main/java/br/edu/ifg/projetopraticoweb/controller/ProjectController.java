package br.edu.ifg.projetopraticoweb.controller;

import br.edu.ifg.projetopraticoweb.enum_data.Profile;
import br.edu.ifg.projetopraticoweb.dto.ProjectDTO;
import br.edu.ifg.projetopraticoweb.exception.ResourceNotFoundException;
import br.edu.ifg.projetopraticoweb.model.Project;
import br.edu.ifg.projetopraticoweb.model.User;
import br.edu.ifg.projetopraticoweb.service.ProjectService;
import br.edu.ifg.projetopraticoweb.service.UserService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Path("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public ProjectController(ProjectService projectService, UserService userService, ModelMapper modelMapper) {
        this.projectService = projectService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    // Exibe a lista de projetos na página Thymeleaf
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String listProjects(Model model) {
        User user = userService.getAuthenticatedUser();
        List<ProjectDTO> projects = projectService.findAllByUser(user).stream()
                .map(this::convertToProjectDTO)
                .collect(Collectors.toList());
        model.addAttribute("projects", projects);
        return "project-list";
    }

    // Exibe o formulário de criação de um novo projeto
    @GET
    @Path("/create")
    @Produces(MediaType.TEXT_HTML)
    public String showCreateForm(Model model) {
        populateModelForForm(model, new ProjectDTO());
        return "project-create";
    }

    // Processa o envio do formulário de criação de projeto
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response createProject(@BeanParam ProjectDTO projectDTO, Model model) {
        return getResponse(projectDTO, model);
    }

    // Exibe o formulário de edição de um projeto existente
    @GET
    @Path("/edit/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String showEditForm(@PathParam("id") Long id, Model model) {
        Optional<Project> project = projectService.findById(id);
        if (project.isPresent()) {
            model.addAttribute("project", convertToProjectDTO(project.get()));
            model.addAttribute("users", userService.findAll());
            return "project-edit";
        } else {
            return "redirect:/projects/";
        }
    }

    // Processa o envio do formulário de edição de projeto
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Response updateProject(@BeanParam ProjectDTO projectDTO, Model model) {
        return getResponse(projectDTO, model);
    }

    // Processa a exclusão de um projeto
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.TEXT_HTML)
    public Response deleteProject(@PathParam("id") Long id) {
        projectService.delete(id);
        return Response.seeOther(URI.create("/projects/")).build();
    }

    // Método para criar a resposta para criação e atualização de projetos
    private Response getResponse(ProjectDTO projectDTO, Model model) {
        if (!isValid(projectDTO)) {
            populateModelForForm(model, projectDTO);
            return Response.status(Response.Status.BAD_REQUEST).entity("Dados inválidos").build();
        }

        try {
            Project project = convertToProject(projectDTO);
            projectService.save(project);
            return Response.seeOther(URI.create("/projects/")).build();
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            populateModelForForm(model, projectDTO);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // Conversão de ProjectDTO para Project
    private Project convertToProject(ProjectDTO projectDTO) {
        Project project = modelMapper.map(projectDTO, Project.class);

        // Obter o usuário autenticado como supervisor e verificar se ele é SUPERVISOR
        User supervisor = userService.getAuthenticatedUser();
        if (supervisor.getProfile() != Profile.SUPERVISOR) {
            throw new SecurityException("Somente usuários com perfil SUPERVISOR podem criar ou gerenciar projetos.");
        }

        // Define o supervisor como o usuário autenticado
        project.setSupervisor(supervisor);

        // Define os participantes do projeto
        project.setParticipants(userService.findAllByIds(projectDTO.getParticipantIds()));

        return project;
    }

    // Conversão de Project para ProjectDTO
    private ProjectDTO convertToProjectDTO(Project project) {
        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);
        projectDTO.setParticipantIds(project.getParticipants().stream()
                .map(User::getId)
                .collect(Collectors.toList()));
        return projectDTO;
    }

    // Popula o modelo com os dados para o formulário
    private void populateModelForForm(Model model, ProjectDTO projectDTO) {
        model.addAttribute("project", projectDTO);
        model.addAttribute("users", userService.findAll());
    }

    // Validações adicionais do ProjectDTO
    private boolean isValid(ProjectDTO projectDTO) {
        if (projectDTO.getName() == null || projectDTO.getName().trim().isEmpty()) {
            return false;
        }

        if (projectDTO.getDescription() != null && projectDTO.getDescription().length() > 2000) {
            return false;
        }

        if (projectDTO.getStartDate() == null || projectDTO.getEndDate() == null) {
            return false;
        }

        return !projectDTO.getEndDate().isBefore(projectDTO.getStartDate());
    }
}
