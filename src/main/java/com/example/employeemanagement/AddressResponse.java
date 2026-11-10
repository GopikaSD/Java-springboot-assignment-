package com.example.employeemanagement;

public record AddressResponse(
        Long id,
        String street,
        String city,
        String state,
        String country,
        String postalCode
) {
}