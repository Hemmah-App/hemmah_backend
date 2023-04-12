package org.help.hemah.repository.help_requset;

import org.help.hemah.domain.help_request.chat.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestMessagePagingRepository extends PagingAndSortingRepository<Message, Long> {
    Page<Message> findAllByChatRoomIdOrderByCreatedAt(Long chatRoomId, Pageable pageable);
}
