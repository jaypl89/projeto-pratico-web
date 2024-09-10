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

    @NotNull
    @Size(min = 1, message = "Title must not be empty")
    private String title;

    @Size(max = 2000, message = "Description should not exceed 2000 characters")
    private String description;

    @NotNull(message = "Deadline is required")
    private String deadline;

    @NotNull(message = "Status is required")
    private String status;

    @NotNull(message = "Project is required")
    private Long projectId;
}
