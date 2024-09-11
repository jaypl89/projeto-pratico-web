package br.edu.ifg.projetopraticoweb.service;

import br.edu.ifg.projetopraticoweb.model.Project;
import br.edu.ifg.projetopraticoweb.model.User;
import br.edu.ifg.projetopraticoweb.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    public Project save(Project project) {
        return projectRepository.save(project);
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

    // Busca os projetos do usuário
    public List<Project> findAllByUser(User user) {
        return projectRepository.findAllByUser(user.getId());
    }
}

