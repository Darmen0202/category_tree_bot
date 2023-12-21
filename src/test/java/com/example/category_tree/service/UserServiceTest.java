package com.example.category_tree.service;
import com.example.category_tree.CategoryTreeApplication;
import com.example.category_tree.entity.Role;
import com.example.category_tree.entity.User;
import com.example.category_tree.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CategoryTreeApplication.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void whenAdminExists_thenShouldBeReturned() {
        Long adminId = 1L;
        User admin = new User();
        admin.setId(adminId);
        admin.setRole(Role.ADMIN);

        when(userRepository.findById(adminId)).thenReturn(Optional.of(admin));

        boolean isAdmin = userService.isUserAdmin(adminId);

        assertTrue(isAdmin, "Пользователь должен быть администратором");
    }
}
