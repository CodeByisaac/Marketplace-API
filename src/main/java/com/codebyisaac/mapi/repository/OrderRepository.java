package com.codebyisaac.mapi.repository;

import com.codebyisaac.mapi.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    @Query("SELECT o FROM Order o JOIN FETCH o.items")
    List<Order> findAllWithItems();

    @Query("SELECT o FROM Order o JOIN FETCH o.items WHERE o.userId = :userId")
    List<Order> findByUserIdWithItems(@Param("userId") String userId);
}