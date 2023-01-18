package org.help.hemah.repository;

import org.help.hemah.model.enums.UserStatus;
import org.help.hemah.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    @Query("SELECT u FROM User u " +
            "JOIN Volunteer v ON v.baseUserDataEntity.username = u.baseUserDataEntity.username " +
            "WHERE u.status = ?1")
    List<Volunteer> findAllByStatus(UserStatus status);
}
