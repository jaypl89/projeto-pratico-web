package br.edu.ifg.projetopraticoweb.controller;

import br.edu.ifg.projetopraticoweb.model.User;
import br.edu.ifg.projetopraticoweb.service.UserService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.List;
import java.util.Optional;

@Controller
@Path("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Exibe a lista de usuários na página Thymeleaf
    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public String listUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list";  // Thymeleaf page to render user list (user-list.html)
    }

    // Exibe o formulário de cadastro de usuário
    @GET
    @Path("/create")
    @Produces(MediaType.TEXT_HTML)
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "user-create";  // Thymeleaf page to render the create user form (user-create.html)
    }

    // Processa o envio do formulário de cadastro
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String createUser(@ModelAttribute User user) {
        userService.save(user);
        return "redirect:/users/";  // Redirect to the user list page after saving
    }

    // Exibe o formulário de edição do usuário
    @GET
    @Path("/edit/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String showEditForm(@PathParam("id") Long id, Model model) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user-edit";  // Thymeleaf page to render the edit user form (user-edit.html)
        } else {
            return "redirect:/users/";  // Redirect to the list if the user does not exist
        }
    }

    // Processa o envio do formulário de atualização
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String updateUser(@ModelAttribute User user) {
        userService.save(user);  // O método save pode ser usado para atualizar, se o ID já existir
        return "redirect:/users/";
    }

    // Processa a exclusão de um usuário
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String deleteUser(@PathParam("id") Long id) {
        userService.delete(id);
        return "redirect:/users/";  // Redirect to the user list after deletion
    }
}


