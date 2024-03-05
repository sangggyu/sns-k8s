package com.example.userserver.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Transactional
    public UserInfo createUser(UserRequest userrequest) {
        String hashedPassword = passwordEncoder.encode(userrequest.getPlainPassword());
        if (userRepository.findByUsername(userrequest.getUsername()) != null) {
            throw new RuntimeException("Username duplicated");
        }

        User user = new User(userrequest.getUsername(), userrequest.getEmail(), hashedPassword);
        User saveUser = userRepository.save(user);

        return new UserInfo(saveUser);
    }

    public UserInfo getUser(int userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return null;
        }

        return new UserInfo(user);
    }

    public UserInfo getUserByName(String name) {
        User user = userRepository.findByUsername(name);
        if (user == null) {
            return null;
        }

        return new UserInfo(user);
    }

    public UserInfo signIn(UserRequest signInRequest) {
        User user = null;
        if (signInRequest.getUsername() != null) {
            user = userRepository.findByUsername(signInRequest.getUsername());
        }

        if (user == null) {
            return null;
        }

        boolean isPasswordMatch = passwordEncoder.matches(signInRequest.getPlainPassword(), user.getPassword());
        if (isPasswordMatch) {
            return new UserInfo(user);
        }
        return null;
    }
}
