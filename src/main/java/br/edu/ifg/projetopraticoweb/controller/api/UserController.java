package br.edu.ifg.projetopraticoweb.controller.api;

import br.edu.ifg.projetopraticoweb.dto.UserDTO;
import br.edu.ifg.projetopraticoweb.mapper.UserMapper;
import br.edu.ifg.projetopraticoweb.model.User;
import br.edu.ifg.projetopraticoweb.service.UserService;
import br.edu.ifg.projetopraticoweb.validator.UserDTOValidator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Path("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserDTOValidator userDTOValidator;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userDTOValidator = new UserDTOValidator();
    }

    // API para listar todos os usuários (retorna JSON)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listUsers() {
        List<UserDTO> users = userService.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
        return Response.ok(users).build();
    }

    // API para obter um usuário por ID
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new WebApplicationException("User not found", 404));
        return Response.ok(userMapper.toDTO(user)).build();
    }

    // API para criar um novo usuário (recebe dados em JSON)
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User createdUser = userService.save(user);
        return Response.status(Response.Status.CREATED)
                .entity(userMapper.toDTO(createdUser))
                .build();
    }

    // API para atualizar um usuário existente (recebe dados em JSON)
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") Long id, UserDTO userDTO) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            User user = userMapper.toEntity(userDTO);
            user.setId(id);
            User updatedUser = userService.save(user);
            return Response.ok(userMapper.toDTO(updatedUser)).build();
        } else {
            throw new WebApplicationException("User not found", 404);
        }
    }

    // API para deletar um usuário por ID
    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        if (userService.findById(id).isPresent()) {
            userService.delete(id);
            return Response.noContent().build();
        } else {
            throw new WebApplicationException("User not found", 404);
        }
    }
}
