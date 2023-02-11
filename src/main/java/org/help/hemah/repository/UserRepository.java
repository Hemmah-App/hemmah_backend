package org.help.hemah.repository;

import org.help.hemah.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END " +
            "FROM User u WHERE u.baseUserDataEntity.email = ?1")
    boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN (COUNT(u) > 0) THEN true ELSE false END " +
            "FROM User u WHERE u.baseUserDataEntity.username = ?1")
    boolean existsByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.baseUserDataEntity.username = ?1")
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.baseUserDataEntity.email = ?1")
    Optional<User> findByEmail(String email);

}
