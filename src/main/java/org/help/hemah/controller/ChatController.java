package org.help.hemah.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.help.hemah.domain.help_request.chat.ChatRoom;
import org.help.hemah.domain.help_request.chat.Message;
import org.help.hemah.helper.req_model.NewMessageModel;
import org.help.hemah.service.chat.RequestChatService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final RequestChatService requestChatService;

    @GetMapping
    public List<ChatRoom> getChatRooms() {
        return requestChatService.getChatRooms();
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equalsIgnoreCase("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equalsIgnoreCase("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }

    @GetMapping("/{chatId}/messages")
    public Page<Message> getMessagesByChatId(@PathVariable Long chatId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "id,desc") String[] sort) {

        List<Sort.Order> orders = new ArrayList<>();

        if (sort[0].contains(",")) {
            // will sort more than 2 fields
            // sortOrder="field, direction"
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            // sort=[field, direction]
            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
        }

        return requestChatService.getChatRoomMessages(chatId,
                PageRequest.of(page, size, Sort.by(orders)));
    }

    @MessageMapping("/messages/send")
    void sendMessage(@RequestBody NewMessageModel messageModel) {
        if (messageModel.getChatId() == null && messageModel.getHelpRequestId() == null) {
            throw new RuntimeException("You must provide chatId or helpRequestId.");
        }


        if (messageModel.getChatId() == null) {
            ChatRoom chatRoom = requestChatService.createNewChatRoom(messageModel.getHelpRequestId());
            log.info("chatroom: " + chatRoom.getId());
            System.out.println();
            messageModel.setChatId(chatRoom.getId());
        }

        requestChatService.sendMessage(messageModel.getChatId(), messageModel.getContent());

    }


}
