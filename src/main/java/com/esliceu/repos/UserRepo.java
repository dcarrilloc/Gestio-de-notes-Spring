package com.esliceu.repos;

import com.esliceu.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmailAndAuthAndPassword(String email, String auth, String password);
    User findByUsername(String username);
}
