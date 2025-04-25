package com.adacorp.corpochat.chat;

import com.adacorp.corpochat.common.BaseAuditingEntity;
import com.adacorp.corpochat.message.Message;
import com.adacorp.corpochat.message.MessageState;
import com.adacorp.corpochat.message.MessageType;
import com.adacorp.corpochat.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chat")
@NamedQuery(name = ChatConstants.FIND_CHAT_BY_SENDER_ID,
    query = "SELECT DISTINCT c FROM Chat c WHERE c.sender.id = :senderId OR c.receiver.id = :senderId ORDER BY createdDate DESC"
)

@NamedQuery(name= ChatConstants.FIND_CHAT_BY_SENDER_ID_AND_RECEIVER,
            query="SELECT DISTINCT c FROM Chat c WHERE (c.sender.id = :senderId AND c.receiver.id = :receiverId) OR (c.sender.id = :receiverId AND c.receiver.id = :senderId) ORDER BY createdDate DESC "
)
public class Chat extends BaseAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER)
    @OrderBy("createdDate DESC")
    private List<Message> messages;

    @Transient
    public String getChatName(String senderId){
        if(receiver.getId().equals(senderId)){
            return sender.getFirstName() + " " + receiver.getFirstName();
        }
        return receiver.getFirstName() + " " + sender.getFirstName();
    }

    @Transient
    public String getTargetChatName(String senderId){
        if(sender.getId().equals(senderId)){
            return sender.getFirstName() + " " + receiver.getFirstName();
        }
        return receiver.getFirstName() + " " + sender.getFirstName();
    }

    @Transient
    public long getUnreadMessages(String senderId){
        return this
                .messages
                .stream()
                .filter(m -> m.getReceiverId().equals(senderId))
                .filter(m -> MessageState.SENT == m.getState())
                .count();
    }

    @Transient
    public String getLastMessage(){
        if (messages != null && !messages.isEmpty()){
            if(messages.getFirst().getType() != MessageType.TEXT){
                return "Attachment";
            }
            return messages.getFirst().getContent();
        }

        return null;
    }

    @Transient
    public LocalDateTime getLastMessageTime(){
        if (messages != null && !messages.isEmpty()){
            return messages.getFirst().getCreatedDate();
        }
        return null;
    }
}
