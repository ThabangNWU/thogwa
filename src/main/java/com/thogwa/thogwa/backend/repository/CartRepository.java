package com.thogwa.thogwa.backend.repository;

import com.thogwa.thogwa.backend.model.Cart;
import com.thogwa.thogwa.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {


}
