package com.example.swithme;

import com.example.swithme.entity.User;
import com.example.swithme.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class SaveTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void saveTest() {
        String encodePassword = passwordEncoder.encode("1234");
        User user = new User("spring", encodePassword, "스프링");
        userRepository.save(user);
    }
}