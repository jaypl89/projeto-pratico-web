package br.edu.ifg.projetopraticoweb.service;

import br.edu.ifg.projetopraticoweb.enum_data.Profile;
import br.edu.ifg.projetopraticoweb.exception.ResourceNotFoundException;
import br.edu.ifg.projetopraticoweb.model.User;
import br.edu.ifg.projetopraticoweb.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = userOptional.get();
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getProfile().getName())
                .build();
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();  // Email do usuário autenticado
        return userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public boolean authenticate(String email) {
        return getAuthenticatedUser().getEmail().equals(email) || isAdmin();
    }

    private boolean isAdmin() {
        return getAuthenticatedUser().getProfile() == Profile.ADMIN;
    }

    public boolean authenticate(Long id) {
        return Objects.equals(getAuthenticatedUser().getId(), id) || isAdmin();
    }
}
