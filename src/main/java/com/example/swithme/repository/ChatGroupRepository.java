package com.example.swithme.repository;

import com.example.swithme.entity.chat.ChatGroup;
import com.example.swithme.entity.chat.ChatRoom;
import com.example.swithme.entity.User;
import com.example.swithme.enumType.ChatRole;
import com.example.swithme.enumType.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatGroupRepository extends JpaRepository<ChatGroup, Long> {

    List<ChatGroup> findAllByUser(User user);

    List<ChatGroup> findAllByChatRoomAndChatRole(ChatRoom chatRoom, ChatRole chatRole);
    List<ChatGroup> findAllByChatRoom(ChatRoom chatRoom);


    List<ChatGroup> findAllByUserAndInvite(User user, Invite invite);

    Optional<ChatGroup> findByChatRoom_IdAndAndUser(Long id, User user);

}
