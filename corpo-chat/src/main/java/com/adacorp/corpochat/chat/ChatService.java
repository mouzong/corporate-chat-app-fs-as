package com.adacorp.corpochat.chat;

import com.adacorp.corpochat.user.User;
import com.adacorp.corpochat.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatMapper chatMapper;

    @Transactional(readOnly = true)
    public List<ChatResponse> getChatsByReceiverId(Authentication currentUser){
        final String currentUserId = currentUser.getName();
        return chatRepository.findChatBySenderId(currentUserId)
                .stream()
                .map(c -> chatMapper.toChatResponse(c, currentUserId))
                .toList();
    }

    public String createChat(String senderId, String receiverId){
        Optional<Chat> optExistingChat = chatRepository.findChatBySenderIdAndReceiverId(senderId, receiverId);

        if(optExistingChat.isPresent()){
            return optExistingChat.get().getId();
        }

        User sender = userRepository.findUserByPublicId(senderId)
                .orElseThrow(() -> new RuntimeException("User with id: "+senderId+" not found..."));

        User receiver = userRepository.findUserByPublicId(receiverId)
                .orElseThrow(() -> new RuntimeException("User with id: "+receiverId+" not found..."));

        Chat chat = new Chat();
        chat.setSender(sender);
        chat.setReceiver(receiver);

        Chat savedChat = chatRepository.save(chat);

        return savedChat.getId();
    }
}
