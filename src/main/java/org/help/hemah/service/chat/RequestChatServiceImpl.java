package org.help.hemah.service.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.help.hemah.domain.disabled.Disabled;
import org.help.hemah.domain.help_request.HelpRequest;
import org.help.hemah.domain.help_request.chat.ChatRoom;
import org.help.hemah.domain.help_request.chat.Message;
import org.help.hemah.domain.user.User;
import org.help.hemah.domain.user.UserType;
import org.help.hemah.domain.volunteer.Volunteer;
import org.help.hemah.repository.help_requset.HelpRequestRepository;
import org.help.hemah.repository.help_requset.RequestChatRoomRepository;
import org.help.hemah.repository.help_requset.RequestMessagePagingRepository;
import org.help.hemah.repository.help_requset.RequestMessageRepository;
import org.help.hemah.service.auth.AuthenticationFacade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestChatServiceImpl implements RequestChatService {

    private final AuthenticationFacade authenticationFacade;
    private final SimpMessagingTemplate messagingTemplate;
    private final HelpRequestRepository helpRequestRepository;
    private final RequestChatRoomRepository chatRoomRepository;
    private final RequestMessagePagingRepository messagePagingRepository;
    private final RequestMessageRepository messageRepository;


    @Override
    public List<ChatRoom> getChatRooms() {
        List<ChatRoom> chatRooms = new ArrayList<>();

        User user = authenticationFacade.getAuthenticatedUser();

        if (user.getType() == UserType.DISABLED) {
            chatRooms = chatRoomRepository.findAllByDisabledId(authenticationFacade.getAuthenticatedDisabled().getId());
        } else if (user.getType() == UserType.VOLUNTEER) {
            chatRooms = chatRoomRepository.findAllByVolunteerId(authenticationFacade.getAuthenticatedVolunteer().getId());
        }

        return chatRooms;
    }

    @Override
    public Page<Message> getChatRoomMessages(Long chatRoomId, Pageable pageable) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("Chat room does not exist"));

        if (!chatRoom.isParty(authenticationFacade.getAuthenticatedUser().getUserData().getUsername())) {
            throw new RuntimeException("You are not authorized to access this.");
        }

        return messagePagingRepository.findAllByChatRoomIdOrderByCreatedAt(chatRoom.getId(), pageable);
    }


    @Override
    public ChatRoom createNewChatRoom(Long helpRequestId) {
        User user = authenticationFacade.getAuthenticatedUser();

        // Verify the existence of the help request
        HelpRequest helpRequest = helpRequestRepository.findById(helpRequestId).orElseThrow(() -> new RuntimeException("Help request not found"));


        if (user.getType() != UserType.VOLUNTEER) {
            throw new RuntimeException("Only volunteers can start new chat rooms");
        }

        Volunteer volunteer = authenticationFacade.getAuthenticatedVolunteer();

        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByHelpRequestIdAndVolunteerId(helpRequestId, volunteer.getId());

        // Return The Existing ChatRoom Or Create New One
        return chatRoomOptional.orElseGet(() -> createChatRoom(helpRequest, volunteer));
    }

    @Override
    public void sendMessage(Long chatRoomId, String messageContent) {
        User user = authenticationFacade.getAuthenticatedUser();

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("Chat room does not exist"));

        Message message = createMessage(chatRoom, user, messageContent);

        if (user.getType() == UserType.VOLUNTEER) {
            Disabled disabled = chatRoom.getDisabled();

            log.info("Sending message to user: " + disabled.getUserData().getUsername());
            log.info("Message: " + message.getContent());
            log.info("dest: " + "/messages/receive/" + chatRoomId);

            messagingTemplate.convertAndSendToUser(disabled.getUserData().getUsername(), "/messages/receive/" + chatRoomId,
                    Map.of("content", message.getContent(),
                            "sender", message.getSender().getUserData().getUsername(),
                            "createdAt", message.getCreatedAt()));
        } else if (user.getType() == UserType.DISABLED) {

            Volunteer volunteer = chatRoom.getVolunteer();

            log.info("Sending message to user: " + volunteer.getUserData().getUsername());

            messagingTemplate.convertAndSendToUser(volunteer.getUserData().getUsername(), "/messages/receive/" + chatRoomId,
                    Map.of("content", message.getContent(),
                            "sender", message.getSender().getUserData().getUsername(),
                            "createdAt", message.getCreatedAt()));
        }

    }

    private ChatRoom createChatRoom(HelpRequest helpRequest, Volunteer volunteer) {
        ChatRoom chatRoom = new ChatRoom(helpRequest.getDisabled(), volunteer, helpRequest);
        return chatRoomRepository.save(chatRoom);
    }

    private Message createMessage(ChatRoom chatRoom, User sender, String content) {
        Message message = new Message(chatRoom, sender, content);
        return messageRepository.save(message);
    }
}
