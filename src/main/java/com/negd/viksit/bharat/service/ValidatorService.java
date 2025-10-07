package com.negd.viksit.bharat.service;

import com.negd.viksit.bharat.dto.UserRegisterDto;
import com.negd.viksit.bharat.exception.BadRequestException;
import com.negd.viksit.bharat.model.master.Department;
import com.negd.viksit.bharat.model.master.Ministry;
import com.negd.viksit.bharat.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ValidatorService {

    private final UserRepository userRepository;

    public ValidatorService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateUserDoesNotExist(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EntityExistsException("Email already registered: " + email);
        }
    }

    public void validateRoleInputConsistency(String roleName, UserRegisterDto dto) {
        String role = roleName.toUpperCase();
        boolean hasMinistry = dto.getMinistryId() != null;
        boolean hasDepartment = dto.getDepartmentId() != null;

        switch (role) {
            case "SUPER_ADMIN", "CBSEC_USER", "PMO_USER" -> {
                if (hasMinistry || hasDepartment) {
                    throw new BadRequestException("Invalid input for role type: " + role);
                }
            }
            case "MINISTRY_ADMIN" -> {
                if (!hasMinistry || hasDepartment) {
                    throw new BadRequestException("Invalid input for role type: " + role);
                }
            }
            case "DEPT_ADMIN" -> {
                if (!hasDepartment) {
                    throw new BadRequestException("Department ID required for DEPT_ADMIN");
                }
            }
        }
    }

    public void validateDeptAdminConsistency(String roleName, UserRegisterDto dto, Department department, Ministry ministry) {
        if (!"DEPT_ADMIN".equalsIgnoreCase(roleName)) return;

        if (dto.getMinistryId() == null) {
            if (department.getMinistry() != null) {
                throw new BadRequestException("Department has associated ministry for department id");
            }
        } else {
            UUID deptMinistryId = department.getMinistry() != null ? department.getMinistry().getId() : null;
            if (!dto.getMinistryId().equals(deptMinistryId)) {
                throw new BadRequestException("Provided ministry id does not match department’s ministry");
            }
        }
    }

}
