package com.pollub.covidimpactontransportapi.data;

import com.pollub.covidimpactontransportapi.entities.AppUser;
import com.pollub.covidimpactontransportapi.entities.Role;
import com.pollub.covidimpactontransportapi.repositories.IRoleRepository;
import com.pollub.covidimpactontransportapi.repositories.IUserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseSeeder implements ApplicationRunner {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DatabaseSeeder(IUserRepository userRepository, IRoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedRoles();
        seedAdmin();
    }

    private void seedRoles() {
        if (roleRepository.findByName("ADMIN") == null) {
            Role adminRole = new Role("ADMIN");
            roleRepository.save(adminRole);
        }
        if (roleRepository.findByName("USER") == null) {
            Role userRole = new Role("USER");
            roleRepository.save(userRole);
        }
        roleRepository.flush();
    }

    private void seedAdmin() {
        if (userRepository.findByUsername("admin").isPresent())
            return;
        var encodedPassword = passwordEncoder.encode("admin");
        AppUser admin = new AppUser("admin", encodedPassword);
        Role adminRole = roleRepository.findByName("ADMIN");
        admin.addRole(adminRole);
        userRepository.saveAndFlush(admin);
    }
}
