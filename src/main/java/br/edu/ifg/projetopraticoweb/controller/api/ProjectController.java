package br.edu.ifg.projetopraticoweb.controller.api;

import br.edu.ifg.projetopraticoweb.dto.ProjectDTO;
import br.edu.ifg.projetopraticoweb.exception.ResourceNotFoundException;
import br.edu.ifg.projetopraticoweb.mapper.ProjectMapper;
import br.edu.ifg.projetopraticoweb.model.Project;
import br.edu.ifg.projetopraticoweb.service.ProjectService;
import br.edu.ifg.projetopraticoweb.validator.ProjectDTOValidator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMapper projectMapper;
    private final ProjectDTOValidator validator;

    public ProjectController(ProjectService projectService, ProjectMapper projectMapper) {
        this.projectService = projectService;
        this.projectMapper = projectMapper;
        this.validator = new ProjectDTOValidator();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listProjects() {
        List<ProjectDTO> projects = projectService.findAll().stream()
                .map(projectMapper::toDTO)
                .collect(Collectors.toList());
        return Response.ok(projects).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findProjectById(@PathParam("id") Long id) {
        Project project = projectService.findById(id).orElseThrow(() -> new WebApplicationException("Project not found", 404));
        return Response.ok(projectMapper.toDTO(project)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProject(ProjectDTO projectDTO) {
        if (!validator.isValid(projectDTO)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Dados inválidos").build();
        }

        try {
            Project project = projectMapper.toEntity(projectDTO);
            projectService.save(project);
            return Response.created(URI.create("/api/projects/" + project.getId())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Erro ao criar projeto: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProject(@PathParam("id") Long id, ProjectDTO projectDTO) {
        if (!validator.isValid(projectDTO)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Dados inválidos").build();
        }

        try {
            Project project = projectMapper.toEntity(projectDTO);
            project.setId(id);
            projectService.save(project);
            return Response.ok().build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Erro ao atualizar projeto: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProject(@PathParam("id") Long id) {
        try {
            projectService.delete(id);
            return Response.noContent().build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Erro ao excluir projeto: " + e.getMessage()).build();
        }
    }

}

