package br.edu.ifg.projetopraticoweb.controller;

import br.edu.ifg.projetopraticoweb.enum_data.Profile;
import br.edu.ifg.projetopraticoweb.dto.UserDTO;
import br.edu.ifg.projetopraticoweb.mapper.UserMapper;
import br.edu.ifg.projetopraticoweb.model.User;
import br.edu.ifg.projetopraticoweb.service.AuthenticationService;
import br.edu.ifg.projetopraticoweb.service.UserService;
import br.edu.ifg.projetopraticoweb.validator.DTOValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final DTOValidator<UserDTO> validator;
    private final AuthenticationService authenticationService;

    public UserController(UserService userService, UserMapper userMapper, AuthenticationService authenticationService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authenticationService = authenticationService;
        this.validator = new DTOValidator<>();
    }

    // API para listar todos os usuários (retorna JSON)
    @GetMapping
    public ResponseEntity<List<UserDTO>> listUsers() {
        List<UserDTO> users = userService.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    // API para obter um usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!"));
        return ResponseEntity.ok(userMapper.toDTO(user));
    }

    // API para criar um novo usuário (recebe dados em JSON)
    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
        if (!validator.isValid(userDTO)) {
            return ResponseEntity.badRequest().body(validator.validationMessages(userDTO).toString());
        }

        User user = userMapper.toEntity(userDTO);
        User createdUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Usuário criado com sucesso com ID: " + createdUser.getId());
    }

    // API para atualizar um usuário existente (recebe dados em JSON)
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        if (!authenticationService.authenticate(id)) {
            return ResponseEntity.badRequest().body("Usuário não autorizado");
        }

        if (!validator.isValid(userDTO)) {
            return ResponseEntity.badRequest().body(validator.validationMessages(userDTO).toString());
        }

        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            User user = userMapper.toEntity(userDTO);
            user.setId(id);
            userService.save(user);
            return ResponseEntity.ok("Usuário atualizado com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
    }

    // Adiciona um método para alternar o perfil do usuário
    @PutMapping("/{id}/toggleProfile")
    public ResponseEntity<String> toggleUserProfile(@PathVariable Long id) {
        if (!authenticationService.authenticate(id)) {
            return ResponseEntity.badRequest().body("Usuário não autorizado");
        }

        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Alterna o perfil entre USER e SUPERVISOR
            if (user.getProfile() == Profile.USER) {
                user.setProfile(Profile.SUPERVISOR);
            } else if (user.getProfile() == Profile.SUPERVISOR) {
                user.setProfile(Profile.USER);
            } else {
                return ResponseEntity.badRequest().body("Perfil desconhecido");
            }
            userService.save(user);
            return ResponseEntity.ok("Perfil do usuário atualizado com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
    }

    // API para deletar um usuário por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (!authenticationService.authenticate(id)) {
            return ResponseEntity.badRequest().body("Usuário não autorizado");
        }

        if (userService.findById(id).isPresent()) {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
    }

}

