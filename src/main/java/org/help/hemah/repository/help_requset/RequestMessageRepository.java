package org.help.hemah.repository.help_requset;

import org.help.hemah.domain.help_request.chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestMessageRepository extends JpaRepository<Message, Long> {
}
