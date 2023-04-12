package org.help.hemah.service.chat;

import org.help.hemah.domain.help_request.chat.ChatRoom;
import org.help.hemah.domain.help_request.chat.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RequestChatService {

    List<ChatRoom> getChatRooms();

    Page<Message> getChatRoomMessages(Long chatRoomId, Pageable pageable);

    ChatRoom createNewChatRoom(Long helpRequestId);

    void sendMessage(Long chatRoomId, String messageContent);


}
