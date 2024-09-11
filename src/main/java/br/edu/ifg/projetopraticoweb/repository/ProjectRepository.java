package br.edu.ifg.projetopraticoweb.repository;

import br.edu.ifg.projetopraticoweb.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Consulta JPQL que retorna projetos onde o usuário é supervisor ou participante
    @Query("SELECT p FROM Project p WHERE p.supervisor.id = :userId OR :userId MEMBER OF p.participants")
    List<Project> findAllByUser(@Param("userId") Long userId);
}

