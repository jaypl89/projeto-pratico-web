package br.edu.ifg.projetopraticoweb.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        packages("br.edu.ifg.projetopraticoweb.controller"); // O pacote onde os controllers JAX-RS estão localizados
    }
}

