package com.thogwa.thogwa.backend.repository;

import com.thogwa.thogwa.backend.model.Order;
import com.thogwa.thogwa.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository  extends JpaRepository<Order, Integer> {
    List<Order> findAllByUserOrderByCreatedDateDesc(User user);
}
