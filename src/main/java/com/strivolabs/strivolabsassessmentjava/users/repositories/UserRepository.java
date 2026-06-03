package com.strivolabs.strivolabsassessmentjava.users.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.strivolabs.strivolabsassessmentjava.common.dtos.UserDto;
import com.strivolabs.strivolabsassessmentjava.users.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {
       @Query("""
                     SELECT Count(u) > 0
                     FROM   User u
                     WHERE  u.email = :email
                            AND u.deletedAt IS NULL
                     """)
       boolean existsByEmail(@Param("email") String email);

       @Query("""
                     SELECT u.id,
                            u.firstName,
                            u.lastName,
                            u.email,
                            u.passwordHash
                     FROM   User u
                     WHERE  u.email = :email
                            AND  u.deletedAt IS NULL
                         """)
       Optional<UserDto> findDtoByEmail(@Param("email") String email);

       Optional<User> findByEmail(String email);

       @Override
       @Query("""
                     SELECT u
                     FROM   User u
                     WHERE  u.Id = :userId
                            AND  u.deletedAt IS NULL
                         """)
       Optional<User> findById(@Param("userId") UUID userId);

}
