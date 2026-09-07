package com.amazonclone.practice.repository;

import com.amazonclone.practice.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUserEmail(String userEmail);
    Optional<Address> findByIdAndUserEmail(Long id, String userEmail);
}