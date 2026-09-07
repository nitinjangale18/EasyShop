package com.amazonclone.practice.service;

import com.amazonclone.practice.dto.AddressRequest;
import com.amazonclone.practice.entity.Address;

import java.util.List;

public interface AddressService {

    Address addAddress(String userEmail, AddressRequest request);

    List<Address> getMyAddresses(String userEmail);
    
    Address getAddressById(Long id, String email);
}

