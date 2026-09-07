package com.amazone_clone.Order_service.repository;


import com.amazone_clone.Order_service.entiry.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserEmail(String userEmail);
    
    Optional<Order> findByIdAndUserEmail(Long id, String userEmail);

    
}
