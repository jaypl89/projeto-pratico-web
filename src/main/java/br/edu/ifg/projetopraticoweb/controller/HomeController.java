package br.edu.ifg.projetopraticoweb.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.springframework.stereotype.Controller;

@Controller
@Path("/")
public class HomeController {

    // Mapeia a página inicial (Home)
    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public String home() {
        return "home/index";  // Renderiza a página home/index.html
    }

    // Mapeia a página de login
    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    public String login() {
        return "authentication/login";  // Renderiza a página authentication/login.html
    }

    // Mapeia a página de registro
    @GET
    @Path("/register")
    @Produces(MediaType.TEXT_HTML)
    public String register() {
        return "authentication/register";  // Renderiza a página authentication/register.html
    }
}

