package br.edu.ifg.projetopraticoweb.controller;

import br.edu.ifg.projetopraticoweb.dto.ProjectDTO;
import br.edu.ifg.projetopraticoweb.exception.ResourceNotFoundException;
import br.edu.ifg.projetopraticoweb.mapper.ProjectMapper;
import br.edu.ifg.projetopraticoweb.model.Project;
import br.edu.ifg.projetopraticoweb.service.AuthenticationService;
import br.edu.ifg.projetopraticoweb.service.ProjectService;
import br.edu.ifg.projetopraticoweb.validator.ProjectDTOValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMapper projectMapper;
    private final ProjectDTOValidator validator;
    private final AuthenticationService authenticationService;

    public ProjectController(ProjectService projectService, ProjectMapper projectMapper, AuthenticationService authenticationService) {
        this.projectService = projectService;
        this.projectMapper = projectMapper;
        this.authenticationService = authenticationService;
        this.validator = new ProjectDTOValidator();
    }

    // Listar todos os projetos
    @GetMapping
    public ResponseEntity<List<ProjectDTO>> listProjects() {
        List<ProjectDTO> projects = projectService.findAll().stream()
                .map(projectMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }

    // Buscar projeto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> findProjectById(@PathVariable Long id) {
        Project project = projectService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        return ResponseEntity.ok(projectMapper.toDTO(project));
    }

    // Criar um novo projeto
    @PostMapping("/new")
    public ResponseEntity<String> createProject(@RequestBody ProjectDTO projectDTO) {
        if (!validator.isValid(projectDTO)) {
            return ResponseEntity.badRequest().body("Dados inválidos");
        }

        try {
            Project project = projectMapper.toEntity(projectDTO);
            project.setSupervisor(authenticationService.getAuthenticatedUser());
            projectService.save(project);
            return ResponseEntity.created(URI.create("/projects/" + project.getId())).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao criar projeto: " + e.getMessage());
        }
    }

    // Atualizar um projeto existente
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) {
        Optional<Project> projectOptional = projectService.findById(id);
        if (!authenticationService.authenticate(projectOptional.orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projeto não encontrado")
        ).getSupervisor().getEmail())) {
            return ResponseEntity.badRequest().body("Usuário não autorizado");
        }

        if (!validator.isValid(projectDTO)) {
            return ResponseEntity.badRequest().body("Dados inválidos");
        }

        try {
            Project project = projectMapper.toEntity(projectDTO);
            project.setId(id);
            project.setSupervisor(projectOptional.get().getSupervisor());
            projectService.save(project);
            return ResponseEntity.ok("Projeto atualizado com sucesso");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar projeto: " + e.getMessage());
        }
    }

    // Deletar um projeto
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        try {
            projectService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao excluir projeto: " + e.getMessage());
        }
    }

}
