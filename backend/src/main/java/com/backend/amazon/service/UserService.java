package com.backend.amazon.service;

import com.backend.amazon.dto.UserRequest;
import com.backend.amazon.dto.UserResponse;
import com.backend.amazon.model.User;
import com.backend.amazon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse createUser(UserRequest request) {
        User user = new User(request.getName(), request.getEmail(), request.getPassword(), request.getPhone());
        User savedUser = userRepository.save(user);
        return new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getPhone());
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getPhone()))
                .collect(Collectors.toList());
    }

    public Optional<UserResponse> getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(u -> new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getPhone()));
    }

    public Optional<UserResponse> getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(u -> new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getPhone()));
    }
}
