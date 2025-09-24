package com.negd.viksit.bharat.dto;

import java.util.Set;

public record UserDto(Long userId, String firstName, String lastName, Set<String> roles, String email,String authToken) {
}
