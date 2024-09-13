package br.edu.ifg.projetopraticoweb.mapper;

import br.edu.ifg.projetopraticoweb.dto.ProjectDTO;
import br.edu.ifg.projetopraticoweb.enum_data.Profile;
import br.edu.ifg.projetopraticoweb.model.Project;
import br.edu.ifg.projetopraticoweb.model.User;
import br.edu.ifg.projetopraticoweb.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProjectMapper implements Mapper<Project, ProjectDTO>{

    private final ModelMapper modelMapper;
    private final UserService userService;

    public ProjectMapper(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @Override
    public ProjectDTO toDTO(Project project) {
        ProjectDTO projectDTO = modelMapper.map(project, ProjectDTO.class);
        projectDTO.setParticipantsNames(project.getParticipants().stream()
                .map(User::getName)
                .collect(Collectors.toList()));
        projectDTO.setParticipantsIds(project.getParticipants().stream()
                .map(User::getId)
                .collect(Collectors.toList()));

        return projectDTO;
    }

    @Override
    public Project toEntity(ProjectDTO projectDTO) {
        Project project = modelMapper.map(projectDTO, Project.class);

        // Define os participantes do projeto
        project.setParticipants(userService.findAllByIds(projectDTO.getParticipantsIds()));

        return project;
    }
}
