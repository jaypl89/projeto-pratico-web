package br.edu.ifg.projetopraticoweb.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DTOValidator<D> {

    private final Validator validator;

    public DTOValidator() {
        // Obtém o ValidatorFactory e o Validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public boolean isValid(D dto) {
        // Verifica se o DTO é nulo
        if (dto == null) {
            return false;
        }

        // Obtém as violações de validação e retorna true se não houver violações
        Set<ConstraintViolation<D>> violations = validator.validate(dto);
        return violations.isEmpty();
    }

    public List<String> validationMessages(D dto) {
        // Verifica se o DTO é nulo
        if (dto == null) {
            return List.of("O DTO não pode ser nulo");
        }

        // Obtém as violações de validação e retorna as mensagens
        Set<ConstraintViolation<D>> violations = validator.validate(dto);
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }
}
