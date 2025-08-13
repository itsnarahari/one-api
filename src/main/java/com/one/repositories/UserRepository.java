package com.one.repositories;

import com.one.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByMobile(String mobile);
    Optional<User> findByMobile(String mobile);
}

