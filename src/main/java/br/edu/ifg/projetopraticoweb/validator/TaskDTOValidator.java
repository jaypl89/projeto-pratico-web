package br.edu.ifg.projetopraticoweb.validator;

import br.edu.ifg.projetopraticoweb.dto.TaskDTO;
import br.edu.ifg.projetopraticoweb.enum_data.Status;
import jakarta.validation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;

public class TaskDTOValidator implements DTOValidator<TaskDTO> {

    private final Validator validator;

    public TaskDTOValidator() {
        // Obtém o ValidatorFactory e o Validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Override
    public boolean isValid(TaskDTO taskDTO) {
        if (taskDTO == null) {
            return false;
        }

        // Verifica as violações de validação
        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);

        if (!violations.isEmpty()) {
            return false;
        }

        // Verificação adicional para deadline
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(taskDTO.getDeadline(), formatter);
        } catch (DateTimeParseException e) {
            return false;
        }

        // Verificação adicional para status
        try {
            Status.fromDescription(taskDTO.getStatus());
        } catch (IllegalArgumentException e) {
            return false;
        }

        // Verificação adicional para projectId
        return taskDTO.getProjectId() != null && taskDTO.getProjectId() > 0;
    }
}

