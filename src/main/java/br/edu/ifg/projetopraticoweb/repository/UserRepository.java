package br.edu.ifg.projetopraticoweb.repository;

import br.edu.ifg.projetopraticoweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Método para encontrar usuário por email
    Optional<User> findByEmail(String email);
}
