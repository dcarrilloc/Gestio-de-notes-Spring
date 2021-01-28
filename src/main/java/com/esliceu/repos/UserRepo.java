package com.esliceu.repos;

import com.esliceu.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    public User findByEmailAndAuthAndPassword(String email, String auth, String password);
}
