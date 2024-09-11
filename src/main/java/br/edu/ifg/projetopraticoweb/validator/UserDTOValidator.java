package br.edu.ifg.projetopraticoweb.validator;


import br.edu.ifg.projetopraticoweb.dto.UserDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public class UserDTOValidator implements DTOValidator<UserDTO> {

    private final Validator validator;

    public UserDTOValidator() {
        // Obtém o ValidatorFactory e o Validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Override
    public boolean isValid(UserDTO userDTO) {
        // Verifica se o DTO é nulo
        if (userDTO == null) {
            return false;
        }

        // Obtém as violações de validação
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);

        // Retorna true se não houver violações
        return violations.isEmpty();
    }
}

