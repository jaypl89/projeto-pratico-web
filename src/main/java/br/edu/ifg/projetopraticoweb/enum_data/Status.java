package br.edu.ifg.projetopraticoweb.enum_data;

import lombok.Getter;

@Getter
public enum Status {
    PENDING("Pendente"),
    IN_PROGRESS("Em Progresso"),
    COMPLETED("Concluída");

    private final String description;

    Status(String descricao) {
        this.description = descricao;
    }

    @Override
    public String toString() {
        return description;
    }

    public static Status fromDescription(String description) {
        for (Status status : Status.values()) {
            if (status.description.equalsIgnoreCase(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Descrição inválida: " + description);
    }
}
