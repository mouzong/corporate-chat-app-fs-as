package com.adacorp.corpochat.chat;

import org.springframework.stereotype.Service;

@Service
public class ChatMapper {
    public ChatResponse toChatResponse(Chat chat, String senderId){
        return ChatResponse.builder()
                .id(chat.getId())
                .name(chat.getChatName(senderId))
                .unreadCount(chat.getUnreadMessages(senderId))
                .lastMessage(chat.getLastMessage())
                .lastMessageTime(chat.getLastMessageTime())
                .isReceiverOnline(chat.getReceiver().isUserOnline())
                .senderId(chat.getSender().getId())
                .receiverId(chat.getReceiver().getId())
                .build();
    }
}
