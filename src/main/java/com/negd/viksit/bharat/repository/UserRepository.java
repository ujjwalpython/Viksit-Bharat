package com.negd.viksit.bharat.repository;

import com.negd.viksit.bharat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select case when exists(select 1 from authentication.user_audit ua where requested_token=:token and is_logout=true) then true else false end as token_exist", nativeQuery = true)
    Boolean checkLogout(String token);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
