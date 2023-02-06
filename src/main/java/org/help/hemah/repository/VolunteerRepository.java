package org.help.hemah.repository;

import org.help.hemah.model.Volunteer;
import org.help.hemah.model.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    @Query("SELECT v FROM Volunteer v " +
            "JOIN User u ON u.baseUserDataEntity.username = v.baseUserDataEntity.username " +
            "WHERE u.status = ?1")
    List<Volunteer> findAllByStatus(UserStatus status);

    @Query("SELECT v FROM Volunteer v WHERE v.baseUserDataEntity.username = ?1")
    Optional<Volunteer> findByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Volunteer v WHERE v.baseUserDataEntity.username = ?1")
    boolean existsByUsername(String username);
}
