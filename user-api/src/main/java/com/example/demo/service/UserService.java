package com.example.demo.service;

import com.example.demo.controller.dto.NewUserDTO;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.TypeRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.entity.Profile;
import com.example.demo.repository.entity.Role;
import com.example.demo.repository.entity.Type;
import com.example.demo.repository.entity.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final RoleRepository roleRepository;
    private final TypeRepository typeRepository;
    private final Set<String> defaultRoles;
    private final TicketServiceClient ticketServiceClient;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, TypeRepository typeRepository, @Value("${app.user.default.roles}") Set<String> defaultRoles, TicketServiceClient ticketServiceClient) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.typeRepository = typeRepository;
        this.defaultRoles = defaultRoles;
        this.ticketServiceClient = ticketServiceClient;
    }

    public void cadastrarUsuario(@Valid NewUserDTO newUser) {

        if (!newUser.password().matches("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$")) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres e conter pelo menos uma letra e um número");
        }

        userRepository.findByEmail(newUser.email())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("Usuário com o email " + newUser.email() + " já existe");
                });

        userRepository.findByHandle(newUser.handle())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("Usuário com o nome " + newUser.handle() + " já existe");
                });

        User user = new User();

        user.setEmail(newUser.email());
        user.setHandle(newUser.handle() != null ? newUser.handle() : generateHandle(newUser.email()));
        user.setPassword(passwordEncoder.encode(newUser.password()));

        Set<Role> roles = new HashSet<>();

        roles.addAll(roleRepository.findByNameIn(defaultRoles));

        Set<Role> additionalRoles = roleRepository.findByNameIn(newUser.roles());
        if (additionalRoles.size() != newUser.roles().size()) {
            throw new IllegalArgumentException("Alguns papéis não existem");
        }

        if (roles.isEmpty()) {
            throw new IllegalArgumentException("O usuário deve ter pelo menos um papel");
        }

        if (newUser.types() != null && !newUser.types().isEmpty()) {
            Set<Type> types = typeRepository.findByNameIn(newUser.types());
            if (types.size() != newUser.types().size()) {
                throw new IllegalArgumentException("Alguns tipos não existem");
            }
            user.setTypes(types);
        }

        user.setRoles(roles);

        Profile profile = new Profile();

        profile.setName(newUser.name());
        profile.setCompany(newUser.company());
        profile.setType(newUser.type() != null ? newUser.type() : Profile.AccountType.FREE);

        profile.setUser(user);
        user.setProfile(profile);

        var savedUser = userRepository.save(user);

        ticketServiceClient.createOnboardingTickets(savedUser);
    }

    private String generateHandle(String email) {
        String[] parts = email.split("@");
        String handle = parts[0];
        int i = 1;
        while (userRepository.existsByHandle(handle)) {
            handle = parts[0] + i++;
        }
        return handle;
    }
}
