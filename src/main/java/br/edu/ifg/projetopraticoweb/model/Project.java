package br.edu.ifg.projetopraticoweb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "projects")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDate creationDate;

    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDate.now();
    }

    @Column(name = "start_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDate startDate;

    @Column(name = "end_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDate endDate;

    // Relacionamento com o supervisor do projeto (um projeto tem um supervisor)
    @ManyToOne
    @JoinColumn(name = "supervisor_id", nullable = false)
    private User supervisor;

    // Relacionamento muitos para muitos com os participantes do projeto
    @ManyToMany
    @JoinTable(
            name = "project_participants",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> participants; // Lista de usuários participantes
}
