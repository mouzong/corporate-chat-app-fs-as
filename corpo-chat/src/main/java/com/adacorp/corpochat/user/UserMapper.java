package com.adacorp.corpochat.user;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .lastSeen(user.getLastSeen())
                .isOnline(user.isUserOnline())
                .build();
    }

    public User fromTokenAttributes(Map<String, Object> attributes) {
        User user = new User();

        if(attributes.containsKey("given_name")){
            user.setFirstName(attributes.get("given_name").toString());
        } else if (attributes.containsKey("nickname")) {
            user.setFirstName(attributes.get("nickname").toString());
        }

        if(attributes.containsKey("family_name")){
            user.setLastName(attributes.get("family_name").toString());
        }

        if (attributes.containsKey("email")){
            user.setEmail(attributes.get("email").toString());
        }

        if(attributes.containsKey("sub")){
            user.setId(attributes.get("sub").toString());
        }

        user.setLastSeen(LocalDateTime.now());

        return user;
    }
}
