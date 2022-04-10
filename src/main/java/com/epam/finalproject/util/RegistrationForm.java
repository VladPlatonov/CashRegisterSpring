package com.epam.finalproject.util;


import com.epam.finalproject.model.User;
import com.epam.finalproject.model.UserRole;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import java.util.Collections;

@Data
public class RegistrationForm {
    @NotEmpty(message = "Login should not be empty")
    private String username;
    @NotEmpty(message = "Password should not be empty")
    private String password;
    @NotEmpty(message = "Name should not be empty")
    private String name;
    @NotEmpty(message = "Surname should not be empty")
    private String surname;

    public User toUser(PasswordEncoder passwordEncoder){
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setSurname(surname);
        user.setUserRole(Collections.singleton(UserRole.USER));
        return user;
    }
}
