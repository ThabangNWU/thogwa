package com.thogwa.thogwa.backend.repository;

import com.thogwa.thogwa.backend.model.User;
import com.thogwa.thogwa.backend.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Integer> {
    List<WishList> findAllByUserOrderByCreatedDateDesc(User user);
}
