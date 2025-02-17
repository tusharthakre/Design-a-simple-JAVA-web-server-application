package com.example.AssignmentBackend.service;

import com.example.AssignmentBackend.model.User;
import com.example.AssignmentBackend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class UserService {
    private static final String JSON_PLACEHOLDER_API = "https://jsonplaceholder.typicode.com/users";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @PostConstruct
    public void loadUsersOnStartup() {
        loadUsersFromApi();
    }

    public void loadUsersFromApi() {
        List<User> users = webClientBuilder.build()
                .get()
                .uri(JSON_PLACEHOLDER_API)
                .retrieve()
                .bodyToFlux(User.class)
                .collectList()
                .block();

        if (users != null) {
            userRepository.saveAll(users);
        }
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    public void deleteUserById(Integer userId) {
        userRepository.deleteById(userId);
    }

    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User saveUser(User user) {
        if (userRepository.existsById(user.getId())) {
            throw new RuntimeException("User with this ID already exists!");
        }
        return userRepository.save(user);
    }

    public User updateUser(Integer userId, User updatedUser) {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));
        updatedUser.setId(userId); // Ensure userId remains unchanged
        return userRepository.save(updatedUser);
    }
}
