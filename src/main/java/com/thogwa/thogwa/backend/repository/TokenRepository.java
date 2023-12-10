package com.thogwa.thogwa.backend.repository;

import com.thogwa.thogwa.backend.model.AuthenticationToken;
import com.thogwa.thogwa.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<AuthenticationToken, Integer> {
    AuthenticationToken findByUser(User user);
    AuthenticationToken findByToken(String token);
}
