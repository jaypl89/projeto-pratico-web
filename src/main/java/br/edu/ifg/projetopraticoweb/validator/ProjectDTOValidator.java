package br.edu.ifg.projetopraticoweb.validator;

import br.edu.ifg.projetopraticoweb.dto.ProjectDTO;
import jakarta.validation.*;
import java.util.Set;

public class ProjectDTOValidator implements DTOValidator<ProjectDTO> {

    private final Validator validator;

    public ProjectDTOValidator() {
        // Obtém o ValidatorFactory e o Validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Override
    public boolean isValid(ProjectDTO projectDTO) {
        if (projectDTO == null) {
            return false;
        }

        // Verifica as violações de validação
        Set<ConstraintViolation<ProjectDTO>> violations = validator.validate(projectDTO);

        if (!violations.isEmpty()) {
            return false;
        }

        // Verificação adicional para a data de término não ser antes da data de início
        if (projectDTO.getStartDate() != null && projectDTO.getEndDate() != null) {
            return !projectDTO.getEndDate().isBefore(projectDTO.getStartDate());
        }

        return true;
    }
}

