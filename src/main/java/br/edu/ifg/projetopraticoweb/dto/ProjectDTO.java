package br.edu.ifg.projetopraticoweb.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {

    private Long id; // Id for UPDATE and DELETE

    @NotNull(message = "O nome não pode ser nulo")
    @Size(min = 1, max = 255, message = "O nome deve ter entre 1 e 255 caracteres")
    private String name;

    @Size(max = 2000, message = "A descrição não deve exceder 2000 caracteres")
    private String description;

    @NotNull(message = "A data de início é obrigatória")
    @PastOrPresent(message = "A data de início deve ser hoje ou no passado")
    private LocalDate startDate;

    @NotNull(message = "A data de término é obrigatória")
    @Future(message = "A data de término deve estar no futuro")
    private LocalDate endDate;

    private List<Long> participantIds; // Lista de IDs dos participantes
}
