
package com.example.employeemanagement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequest(

        @NotBlank(message = "Street is required")
        @Size(max = 150)
        String street,

        @NotBlank(message = "City is required")
        @Size(max = 100)
        String city,

        @NotBlank(message = "State is required")
        @Size(max = 100)
        String state,

        @NotBlank(message = "Country is required")
        @Size(max = 100)
        String country,

        @NotBlank(message = "Postal code is required")
        @Size(max = 20)
        String postalCode

) {
}
