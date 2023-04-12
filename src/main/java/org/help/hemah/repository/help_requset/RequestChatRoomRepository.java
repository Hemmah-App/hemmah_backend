package org.help.hemah.repository.help_requset;

import org.help.hemah.domain.help_request.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByHelpRequestIdAndVolunteerId(Long helpRequestId, Long volunteerId);

    List<ChatRoom> findAllByDisabledId(Long disabledId);

    List<ChatRoom> findAllByVolunteerId(Long volunteerId);
}
