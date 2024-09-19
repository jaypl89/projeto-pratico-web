package br.edu.ifg.projetopraticoweb.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private Long id; // Id for UPDATE and DELETE

    @NotNull(message = "O título não pode ser nulo")
    @Size(min = 1, message = "O título não pode estar vazio")
    private String title;

    @Size(max = 2000, message = "A descrição não deve exceder 2000 caracteres")
    private String description;

    @NotNull(message = "O prazo é obrigatório")
    private String deadline;

    @NotNull(message = "O status é obrigatório")
    private String status;

    private Long projectId;
}
