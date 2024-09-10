package br.edu.ifg.projetopraticoweb.service;

import br.edu.ifg.projetopraticoweb.model.Audit;
import br.edu.ifg.projetopraticoweb.repository.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    private final AuditRepository auditRepository;

    public AuditService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public List<Audit> findAll() {
        return auditRepository.findAll();
    }

    public Audit save(Audit audit) {
        return auditRepository.save(audit);
    }

    public void delete(Long id) {
        auditRepository.deleteById(id);
    }
}

