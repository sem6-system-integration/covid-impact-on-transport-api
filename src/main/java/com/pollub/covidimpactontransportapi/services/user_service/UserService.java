package com.pollub.covidimpactontransportapi.services.user_service;

import com.pollub.covidimpactontransportapi.entities.AppUser;
import com.pollub.covidimpactontransportapi.entities.Role;
import com.pollub.covidimpactontransportapi.exceptions.BadRequestException;
import com.pollub.covidimpactontransportapi.exceptions.NotFoundException;
import com.pollub.covidimpactontransportapi.models.requests.CreateUserRequest;
import com.pollub.covidimpactontransportapi.repositories.IUserRepository;
import com.pollub.covidimpactontransportapi.services.role_service.IRoleService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService, UserDetailsService {

    final private IUserRepository userRepository;
    final private IRoleService roleService;
    final private BCryptPasswordEncoder passwordEncoder;

    public UserService(IUserRepository userRepository, IRoleService roleService, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public Long getAuthId() {
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

    @Override
    public List<AppUser> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public AppUser findUserById(Long id) throws NotFoundException {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("User with requested id not found"));
    }

    @Override
    public AppUser createUser(CreateUserRequest request) throws BadRequestException {
        var usernameTaken = userRepository.existsByUsername(request.getUsername());
        if (usernameTaken)
            throw new BadRequestException("User with specified username already exists");

        var user = new AppUser();
        var encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setUsername(request.getUsername());
        user.setPassword(encodedPassword);

        Role userRole = roleService.getRoleByName("USER");
        user.addRole(userRole);

        Role requestedRole = roleService.getRoleByName(request.getAccountType());
        if (requestedRole == null) {
            throw new BadRequestException("Role with specified name does not exist");
        }
        user.addRole(requestedRole);

        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
