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
        seedUsers();
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
        if (roleRepository.findByName("STANDARD") == null) {
            Role userRole = new Role("STANDARD");
            roleRepository.save(userRole);
        }
        if (roleRepository.findByName("PREMIUM") == null) {
            Role userRole = new Role("PREMIUM");
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

    private void seedUsers() {
        if (userRepository.findByUsername("standard").isEmpty()) {
            var encodedPassword = passwordEncoder.encode("standard");
            AppUser standardUser = new AppUser("standard", encodedPassword);
            Role userRole = roleRepository.findByName("USER");
            standardUser.addRole(userRole);
            Role standardRole = roleRepository.findByName("STANDARD");
            standardUser.addRole(standardRole);
            userRepository.saveAndFlush(standardUser);
        }

        if (userRepository.findByUsername("premium").isEmpty()) {
            var encodedPassword = passwordEncoder.encode("premium");
            AppUser premiumUser = new AppUser("premium", encodedPassword);
            Role userRole = roleRepository.findByName("USER");
            premiumUser.addRole(userRole);
            Role premiumRole = roleRepository.findByName("PREMIUM");
            premiumUser.addRole(premiumRole);
            userRepository.saveAndFlush(premiumUser);
        }
    }
}
