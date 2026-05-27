package com.strivolabs.strivolabsassessmentjava.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.strivolabs.strivolabsassessmentjava.users.entities.UserRole;
import com.strivolabs.strivolabsassessmentjava.users.entities.UserRoleId;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
}
