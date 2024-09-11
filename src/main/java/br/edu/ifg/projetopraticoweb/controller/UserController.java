package br.edu.ifg.projetopraticoweb.controller;

import br.edu.ifg.projetopraticoweb.dto.UserDTO;
import br.edu.ifg.projetopraticoweb.exception.ResourceNotFoundException;
import br.edu.ifg.projetopraticoweb.mapper.UserMapper;
import br.edu.ifg.projetopraticoweb.model.User;
import br.edu.ifg.projetopraticoweb.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")  // Define a rota base "/users" para todos os métodos
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    // Exibe a lista de usuários na página Thymeleaf
    @GetMapping("/list")
    public String listUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "user/list";  // Renderiza a página user-list.html
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID " + id));
        model.addAttribute("user", user);
        return "user/detail";  // Renderiza a página user-detail.html
    }

    // Exibe o formulário de cadastro de usuário
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "user/create";  // Renderiza a página user-create.html
    }

    // Exibe o formulário de edição do usuário
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            UserDTO userDTO = userMapper.toDTO(user.get());
            model.addAttribute("userDTO", userDTO);
            return "user/edit";  // Renderiza a página user-edit.html
        } else {
            return "redirect:/";  // Redireciona se o usuário não for encontrado
        }
    }

}
