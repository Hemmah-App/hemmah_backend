package org.help.hemah.repository;

import org.help.hemah.model.help_video_call.HelpVideoCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HelpVideoCallRepository extends JpaRepository<HelpVideoCall, Long> {
    Optional<HelpVideoCall> findByRoomName(String roomName);
}
