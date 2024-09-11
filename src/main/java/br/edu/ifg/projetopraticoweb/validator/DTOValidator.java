package br.edu.ifg.projetopraticoweb.validator;

public interface DTOValidator<D>{
    boolean isValid(D dto);
}
