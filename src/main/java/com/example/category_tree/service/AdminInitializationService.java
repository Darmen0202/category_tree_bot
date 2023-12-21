package com.example.category_tree.service;

import com.example.category_tree.entity.Role;
import com.example.category_tree.entity.User;
import com.example.category_tree.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminInitializationService {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void addAdminIfNotExist() {
        Long myTelegramId = 933005813L;
        Optional<User> userOpt = userRepository.findById(myTelegramId);
        if (!userOpt.isPresent()) {
            User newUser = new User();
            newUser.setId(myTelegramId);
            newUser.setRole(Role.ADMIN);
            newUser.setUsername("Darmen");
            userRepository.save(newUser);
        }
    }
}
