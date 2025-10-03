package com.negd.viksit.bharat.service;

import com.negd.viksit.bharat.dto.UserRegisterDto;
import com.negd.viksit.bharat.dto.UserRegisterResDto;
import com.negd.viksit.bharat.model.Role;
import com.negd.viksit.bharat.model.User;
import com.negd.viksit.bharat.model.master.Department;
import com.negd.viksit.bharat.model.master.Ministry;
import com.negd.viksit.bharat.repository.DepartmentRepository;
import com.negd.viksit.bharat.repository.MinistryRepository;
import com.negd.viksit.bharat.repository.RoleRepository;
import com.negd.viksit.bharat.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class  UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MinistryRepository ministryRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegisterResDto registerUser(UserRegisterDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new EntityNotFoundException("Email already registered: " + requestDto.getEmail());
        }

        Role role = roleRepository.findByName(requestDto.getUserType())
                .orElseThrow(() -> new EntityNotFoundException("Invalid role: " + requestDto.getUserType()));

        User user = User.builder()
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .email(requestDto.getEmail())
                .mobileNumber(requestDto.getMobileNumber())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .isActive(true)
                .roles(Set.of(role))
                .build();

        if (requestDto.getMinistryId() != null) {
            Ministry ministry = ministryRepository.findById(requestDto.getMinistryId())
                    .orElseThrow(() -> new EntityNotFoundException("Invalid ministry id"));
            user.setMinistry(ministry);
        }

        if (requestDto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(requestDto.getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Invalid department id"));
            user.setDepartment(department);
        }

        User saved = userRepository.save(user);

        return UserRegisterResDto.builder()
                .id(saved.getEntityid())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .email(saved.getEmail())
                .userType(role.getName())
                .isActive(saved.getIsActive())
                .build();
    }
}

