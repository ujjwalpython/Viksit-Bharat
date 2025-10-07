package com.negd.viksit.bharat.service;

import com.negd.viksit.bharat.dto.UserRegisterDto;
import com.negd.viksit.bharat.dto.UserRegisterResDto;
import com.negd.viksit.bharat.exception.BadRequestException;
import com.negd.viksit.bharat.model.Role;
import com.negd.viksit.bharat.model.User;
import com.negd.viksit.bharat.model.master.Department;
import com.negd.viksit.bharat.model.master.Ministry;
import com.negd.viksit.bharat.repository.DepartmentRepository;
import com.negd.viksit.bharat.repository.MinistryRepository;
import com.negd.viksit.bharat.repository.RoleRepository;
import com.negd.viksit.bharat.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MinistryRepository ministryRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidatorService validatorService;

    public UserRegisterResDto registerUser(UserRegisterDto requestDto) {
        log.info("Starting user registration for email: {}", requestDto.getEmail());

        validatorService.validateUserDoesNotExist(requestDto.getEmail());
        log.info("Email {} is available", requestDto.getEmail());

        Role role = getRole(requestDto.getUserType());
        log.info("Assigned role {} for email {}", role.getName(), requestDto.getEmail());

        validatorService.validateRoleInputConsistency(role.getName(), requestDto);
        log.info("Role input consistency validated for role {}", role.getName());

        Ministry ministry = getMinistryIfExists(requestDto.getMinistryId());
        if (ministry != null) {
            log.info("Ministry found: {} for user {}", ministry.getName(), requestDto.getEmail());
        } else {
            log.info("No ministry assigned for user {}", requestDto.getEmail());
        }

        Department department = getDepartmentIfExists(requestDto.getDepartmentId(), ministry);
        if (department != null) {
            log.info("Department found: {} for user {}", department.getName(), requestDto.getEmail());
        } else {
            log.info("No department assigned for user {}", requestDto.getEmail());
        }

        validatorService.validateDeptAdminConsistency(role.getName(), requestDto, department, ministry);
        log.info("Department Admin consistency validated for role {}", role.getName());

        User user = buildUserEntity(requestDto, role, ministry, department);
        log.info("User entity built for email {}", requestDto.getEmail());

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getEntityid());

        UserRegisterResDto response = buildResponseDto(savedUser, role);
        log.info("Response DTO built for user {}", savedUser.getEmail());

        return response;
    }

    private Role getRole(String userType) {
        Role role = roleRepository.findByName(userType.toUpperCase())
                .orElseThrow(() -> new EntityNotFoundException("Invalid input role: " + userType));
        log.info("Fetched role {} from repository", role.getName());
        return role;
    }

    private Ministry getMinistryIfExists(UUID ministryId) {
        if (ministryId == null) return null;
        Ministry ministry = ministryRepository.findById(ministryId)
                .orElseThrow(() -> new EntityNotFoundException("Invalid ministry id: " + ministryId));
        log.info("Fetched ministry {} from repository", ministry.getName());
        return ministry;
    }

    private Department getDepartmentIfExists(UUID departmentId, Ministry ministry) {
        if (departmentId == null) return null;

        Department department;
        if (ministry != null) {
            department = departmentRepository.findByMinistryAndId(ministry, departmentId)
                    .orElseThrow(() -> new EntityNotFoundException("Invalid department id: " + departmentId));
        } else {
            department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new EntityNotFoundException("Invalid department id: " + departmentId));
        }

        log.info("Fetched department {} from repository", department.getName());
        return department;
    }

    private User buildUserEntity(UserRegisterDto dto, Role role, Ministry ministry, Department department) {
        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .mobileNumber(dto.getMobileNumber())
                .password(passwordEncoder.encode(dto.getPassword()))
                .isActive(true)
                .roles(Set.of(role))
                .ministry(ministry)
                .department(department)
                .build();

        log.info("Built User entity for email {}", dto.getEmail());
        return user;
    }

    private UserRegisterResDto buildResponseDto(User user, Role role) {
        UserRegisterResDto response = UserRegisterResDto.builder()
                .id(user.getEntityid())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .userType(role.getName())
                .isActive(user.getIsActive())
                .build();

        log.info("Built Response DTO for user {}", user.getEmail());
        return response;
    }
}

