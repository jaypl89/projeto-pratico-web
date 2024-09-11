package br.edu.ifg.projetopraticoweb.mapper;

import br.edu.ifg.projetopraticoweb.dto.UserDTO;
import br.edu.ifg.projetopraticoweb.model.User;
import br.edu.ifg.projetopraticoweb.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserMapper implements Mapper<User, UserDTO> {

    private final ModelMapper modelMapper;
    private final UserService userService;

    public UserMapper(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @Override
    public UserDTO toDTO(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setPassword("");  // Esvaziar a senha ao enviar para o frontend
        return userDTO;
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        Optional<User> user = userService.findByEmail(userDTO.getEmail());
        return user.orElseGet(() -> modelMapper.map(userDTO, User.class));
    }
}
