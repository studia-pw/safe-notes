package com.odas.safenotes.repositories;

import com.odas.safenotes.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
