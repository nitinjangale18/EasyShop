package com.amazone_clone.Order_service.dto;

public class AddressResponse {

    private Long id;
    private String userEmail;
    private String fullName;
    private String phoneNumber;
    private String addressLine;
    private String city;
    private String state;
    private String pincode;
    private String addressType;
    private boolean isDefault;

    public AddressResponse() {
    }

    public Long getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPincode() {
        return pincode;
    }

    public String getAddressType() {
        return addressType;
    }

    public boolean isDefault() {
        return isDefault;
    }
}