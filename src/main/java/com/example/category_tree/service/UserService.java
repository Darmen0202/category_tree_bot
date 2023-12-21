package com.example.category_tree.service;

import com.example.category_tree.entity.Role;
import com.example.category_tree.entity.User;
import com.example.category_tree.exceptions.UsernameNotFoundException;
import com.example.category_tree.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean isUserAdmin(Long userId) {
        return userRepository.findById(userId)
                .map(User::getRole)
                .map(Role.ADMIN::equals)
                .orElse(false);
    }
    

    public boolean isUser(Long userId) {
        return userRepository.findById(userId)
                .map(User::getRole)
                .map(Role.USER::equals)
                .orElse(false);
    }

    public User registerUser(Long telegramId, String username) {
        User user = new User();
        user.setId(telegramId);
        user.setUsername(username);
        user.setRole(Role.USER); // По умолчанию все новые пользователи - USER
        return userRepository.save(user);
    }

    public User changeUserRole(Long telegramId, Role role) {
        try {
            return userRepository.findById(telegramId)
                    .map(user -> {
                        user.setRole(role);
                        return userRepository.save(user);
                    })
                    .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        } catch (UsernameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
