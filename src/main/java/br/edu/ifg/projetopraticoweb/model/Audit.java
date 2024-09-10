package br.edu.ifg.projetopraticoweb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "audits")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDate creationDate;

    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDate.now();
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
