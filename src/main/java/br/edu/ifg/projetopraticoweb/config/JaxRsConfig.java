package br.edu.ifg.projetopraticoweb.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.springframework.stereotype.Component;

@Component
@ApplicationPath("/api") // Definindo o caminho base para as APIs
public class JaxRsConfig extends Application {
    // As classes de recursos JAX-RS serão registradas automaticamente
}

