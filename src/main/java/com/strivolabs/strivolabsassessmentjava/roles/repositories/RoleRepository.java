package com.strivolabs.strivolabsassessmentjava.roles.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.strivolabs.strivolabsassessmentjava.roles.entities.Role;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    @Query("""
            SELECT r.id
            FROM   Role r
            WHERE  r.name = :name
                  AND r.deletedAt IS NULL
            """)
    Optional<UUID> findIdByName(@Param("name") String name);

    @Query("""
            SELECT r.name
            FROM   Role r
                   inner join UserRole ur
                           ON ur.id.roleId = r.id
            WHERE  ur.id.userId = :userId
                   AND ur.deletedAt IS NULL
                   AND r.deletedAt IS NULL
            """)
    List<String> findRoleNamesByUserId(@Param("userId") UUID userId);
}
