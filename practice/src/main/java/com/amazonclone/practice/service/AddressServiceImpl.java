package com.amazonclone.practice.service;

import com.amazonclone.practice.dto.AddressRequest;
import com.amazonclone.practice.entity.Address;
import com.amazonclone.practice.repository.AddressRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address addAddress(
            String userEmail,
            AddressRequest request
    ) {

        Address address = new Address();

        address.setUserEmail(userEmail);
        address.setFullName(request.getFullName());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setAddressLine(request.getAddressLine());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setAddressType(request.getAddressType());

        // For now every new address is not default
        address.setDefault(false);

        return addressRepository.save(address);
    }

    @Override
    public List<Address> getMyAddresses(String userEmail) {

        return addressRepository.findByUserEmail(userEmail);
    }
    
    @Override
    public Address getAddressById(Long id, String email) {

        return addressRepository
                .findById(id)
                .filter(address -> address.getUserEmail().equals(email))
                .orElseThrow(() ->
                        new RuntimeException("Address not found")
                );
    }
}