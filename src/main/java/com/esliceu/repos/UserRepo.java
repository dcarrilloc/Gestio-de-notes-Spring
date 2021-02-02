package com.esliceu.repos;

import com.esliceu.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmailAndAuthAndPassword(String email, String auth, String password);
    User findByEmailAndAuth(String email, String auth);
    User findByUsername(String username);
}
