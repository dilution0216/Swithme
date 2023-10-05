package com.example.swithme.repository;

import com.example.swithme.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByChatUrl(UUID chatUrl);

}
