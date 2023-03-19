package org.help.hemah.repository;

import org.help.hemah.model.disabled.Disabled;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisabledRepository extends JpaRepository<Disabled, Long> {

    @Query("SELECT d FROM Disabled d WHERE d.baseUserDataEntity.username = ?1")
    Optional<Disabled> findByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Disabled d WHERE d.baseUserDataEntity.username = ?1")
    boolean existsByUsername(String username);

}
