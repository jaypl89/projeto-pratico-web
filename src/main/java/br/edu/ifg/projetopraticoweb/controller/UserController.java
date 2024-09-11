package br.edu.ifg.projetopraticoweb.controller;

import br.edu.ifg.projetopraticoweb.dto.UserDTO;
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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.List;
import java.util.Optional;

@Controller
@Path("/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
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
        model.addAttribute("userDTO", new UserDTO());
        return "user-create";  // Thymeleaf page to render the create user form (user-create.html)
    }

    // Processa o envio do formulário de cadastro
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String createUser(@ModelAttribute UserDTO userDTO) {
        User user = convertToUser(userDTO);
        userService.save(user);
        return "redirect:/users/";  // Redirect to the user list page after saving
    }

    // Exibe o formulário de edição do usuário
    @GET
    @Path("/edit/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String showEditForm(@PathParam("id") Long id, Model model) {
        Optional<User> user = userService.findById(id);

        // Verifica se o usuário autenticado tem permissão para editar
        if (user.isPresent() && isUserAuthorized(id)) {
            UserDTO userDTO = convertToUserDTO(user.get());
            model.addAttribute("userDTO", userDTO);
            return "user-edit";  // Thymeleaf page to render the edit user form (user-edit.html)
        } else {
            return "redirect:/users/";  // Redirect to the list if the user does not exist or if not authorized
        }
    }

    // Processa o envio do formulário de atualização
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String updateUser(@ModelAttribute UserDTO userDTO) {
        User authenticatedUser = userService.getAuthenticatedUser();

        // Verifica se o usuário autenticado está tentando atualizar seu próprio perfil
        if (userDTO.getEmail().equals(authenticatedUser.getEmail())) {
            User user = convertToUser(userDTO);
            userService.save(user);
            return "redirect:/users/";
        } else {
            return "redirect:/users/";  // Redireciona se o usuário não estiver autorizado
        }
    }

    // Processa a exclusão de um usuário
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.TEXT_HTML)
    public String deleteUser(@PathParam("id") Long id) {
        User authenticatedUser = userService.getAuthenticatedUser();

        // Verifica se o usuário autenticado está tentando deletar seu próprio perfil
        if (id.equals(authenticatedUser.getId())) {
            userService.delete(id);
            return "redirect:/users/";
        } else {
            return "redirect:/users/";  // Redireciona se o usuário não estiver autorizado
        }
    }

    // Verifica se o usuário autenticado tem permissão para editar ou deletar
    private boolean isUserAuthorized(Long userId) {
        User authenticatedUser = userService.getAuthenticatedUser();
        return authenticatedUser.getId().equals(userId);
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setPassword("");
        return userDTO;
    }

    private User convertToUser(UserDTO userDTO) {
        Optional<User> user = userService.findByEmail(userDTO.getEmail());
        return user.orElseGet(() -> modelMapper.map(userDTO, User.class));
    }
}

