package br.edu.ifg.projetopraticoweb.model;

import br.edu.ifg.projetopraticoweb.enum_data.Profile;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 90)
    private String name;

    @Column(nullable = false, unique = true, length = 90)
    @Pattern(regexp = "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "Email inválido")
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Profile profile;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDate creationDate;

    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDate.now();
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Task> tasks;

    @OneToMany(mappedBy = "supervisor", cascade = CascadeType.ALL)
    private List<Project> supervisedProjects;

    @ManyToMany(mappedBy = "participants")
    private List<Project> projectsParticipated; // Lista de projetos em que o usuário participa
}
