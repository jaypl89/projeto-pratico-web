package br.edu.ifg.projetopraticoweb.repository;

import br.edu.ifg.projetopraticoweb.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {
}