package com.todoapp.todo;


import com.todoapp.todo.model.Category;
import com.todoapp.todo.model.Role;
import com.todoapp.todo.model.User;
import com.todoapp.todo.repository.CategoryRepository;
import com.todoapp.todo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedAdminUser();
        seedCategory("Sports");
        seedCategory("Media");
        seedCategory("Education");
    }

    private void seedAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("System Admin")
                    .email("admin@example.com")
                    .roles(Set.of(Role.ADMIN, Role.USER))
                    .active(true)
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();

            userRepository.save(admin);
        }
    }

    private void seedCategory(String name) {
        if (categoryRepository.findByNameIgnoreCase(name).isEmpty()) {
            Category category = Category.builder()
                    .name(name)
                    .active(true)
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();

            categoryRepository.save(category);
        }
    }
}
