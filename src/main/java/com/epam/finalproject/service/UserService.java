package com.epam.finalproject.service;

import com.epam.finalproject.model.User;
import com.epam.finalproject.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username);
    }

    public Page<User> findAll(Pageable pageable){
        return userRepository.findAll(pageable);
    }

    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid user Id:" + id));
    }

    public void deleteById(Long id){
        userRepository.deleteById(id);
    }

    public boolean isValidLogin(String login){
        return userRepository.findUserByUsername(login) != null;
    }


    public void save(User user) {
        userRepository.save(user);
    }
}
