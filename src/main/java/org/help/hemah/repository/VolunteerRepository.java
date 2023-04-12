package org.help.hemah.repository;

import org.help.hemah.domain.user.UserStatus;
import org.help.hemah.domain.volunteer.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    @Query("SELECT v FROM Volunteer v " +
            "JOIN User u ON u.baseUserDataEntity.username = v.baseUserDataEntity.username " +
            "WHERE u.status = ?1")
    List<Volunteer> findAllByStatus(UserStatus status);

    @Query("SELECT v FROM Volunteer v WHERE v.baseUserDataEntity.username = ?1")
    Optional<Volunteer> findByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Volunteer v WHERE v.baseUserDataEntity.username = ?1")
    boolean existsByUsername(String username);

    @Query("SELECT COUNT(v) FROM HelpVideoCall hvc " +
            "JOIN Volunteer v ON v.baseUserDataEntity.username = hvc.volunteer.baseUserDataEntity.username " +
            "WHERE v.baseUserDataEntity.username = ?1")
    Long countVolunteerHelpVideoCallsByUsername(String username);
//    Long countVolunteerCompletedHelpRequestByUsername(String username);
}
