package com.negd.viksit.bharat.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCredentialDto(
		@NotBlank(message = "Email is required") @Email(message = "Email is required") String email,
		@NotBlank(message = "Password is required") String password) {
}