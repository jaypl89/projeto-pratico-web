package br.edu.ifg.projetopraticoweb.repository;

import br.edu.ifg.projetopraticoweb.model.Task;
import br.edu.ifg.projetopraticoweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
}

