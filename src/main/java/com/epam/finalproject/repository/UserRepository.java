package com.epam.finalproject.repository;

import com.epam.finalproject.model.User;
import com.epam.finalproject.model.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findUserByUsername(String login);
    Page<User> findAll(Pageable pageable);
}
