package com.example.swithme.service;

import com.example.swithme.dto.chat.*;
import com.example.swithme.entity.User;
import com.example.swithme.entity.chat.ChatGroup;
import com.example.swithme.entity.chat.ChatMessage;
import com.example.swithme.entity.chat.ChatRoom;
import com.example.swithme.enumType.ChatRole;
import com.example.swithme.enumType.Invite;
import com.example.swithme.repository.ChatGroupRepository;
import com.example.swithme.repository.ChatMessageRepository;
import com.example.swithme.repository.ChatRoomRepository;
import com.example.swithme.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final UserRepository userRepository;
    private final ChatGroupRepository chatGroupRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    //DB에 전체 채팅 메시지 저장
    public void save(String content, User user) {
        ChatRoom chatRoom = chatRoomRepository.findById(1L).get();
        ChatMessage chatMessage = new ChatMessage(content, chatRoom, user);
        chatMessageRepository.save(chatMessage);
    }

    //DB에 그룹 채팅 메시지 저장
    public void groupChatSave(User user, String chatUrl, String content) {
        UUID uuid = null;
        try {
            uuid = UUID.fromString(chatUrl);
            System.out.println("변환된 UUID: " + uuid);
        } catch (IllegalArgumentException e) {
            System.out.println("올바르지 않은 UUID 형식입니다.");
        }


        ChatRoom chatRoom = chatRoomRepository.findByChatUrl(uuid).get();
        ChatMessage chatMessage = new ChatMessage(content, chatRoom, user);
        chatMessageRepository.save(chatMessage);
    }


    // DB에 저장된 이전 전체채팅내용 보내기
    public List<ChatMessageResponseDto> findMessage() {
        ChatRoom chatRoom = chatRoomRepository.findById(1L).get();
        List<ChatMessageResponseDto> allMessage = chatMessageRepository.findAllByChatRoom(chatRoom).stream()
                .map(ChatMessageResponseDto::new)
                .collect(Collectors.toList());
        return allMessage ;
    }

    // DB에 저장된 이전 그룹채팅내용 보내기
    public List<ChatMessageResponseDto> findGroupMessage(String chatUrl) {
        UUID uuid = null;
        try {
            uuid = UUID.fromString(chatUrl);
            System.out.println("변환된 UUID: " + uuid);
        } catch (IllegalArgumentException e) {
            System.out.println("올바르지 않은 UUID 형식입니다.");
        }

        ChatRoom chatRoom = chatRoomRepository.findByChatUrl(uuid).get();
        List<ChatMessageResponseDto> allMessage = chatMessageRepository.findAllByChatRoom(chatRoom).stream()
                .map(ChatMessageResponseDto::new)
                .collect(Collectors.toList());
        return allMessage;
    }

    // 내가 가입된 채팅방 조회
    public List<ChatRoomResponseDto> findAllById(User user) {
        // 채팅그룹에서 내가 초대를 승락한 모든 리스트를 가져옴 Invite.YES
        List<ChatRoom> findChatGroup =
                chatGroupRepository.findAllByUserAndInvite(user, Invite.YES).stream()
                        .map(chatGroup -> chatGroup.getChatRoom())
                        .collect(Collectors.toList());

        // DTO로 변환
        List<ChatRoomResponseDto> findAllChatRoom = findChatGroup.stream()
                .map(ChatRoomResponseDto::new)
                .collect(Collectors.toList());
        return findAllChatRoom;
    }

    //초대 알림 페이지 조회
    public List<ChatRoomResponseDto> findAllInvite(User user) {
        // 채팅그룹에서 나를 초대한 모든 리스트 가져옴 Invite.WAIT
        List<ChatRoom> findChatGroup =
                chatGroupRepository.findAllByUserAndInvite(user, Invite.WAIT).stream()
                        .map(chatGroup -> chatGroup.getChatRoom())
                        .collect(Collectors.toList());

        // DTO로 변환
        List<ChatRoomResponseDto> findAllChatRoom = findChatGroup.stream()
                .map(ChatRoomResponseDto::new)
                .collect(Collectors.toList());
        return findAllChatRoom;
    }

    //채팅룸 생성
    public void create(User user) {
        //채팅룸 생성
        ChatRoom chatRoom = new ChatRoom("제목", "내용", UUID.randomUUID());
        chatRoomRepository.save(chatRoom);

        //생성한 채팅룸을 채팅그룹과 유저로 연관관계 매핑하고, 매니저 권한을 부여
        ChatGroup chatGroup = new ChatGroup(user, chatRoom, ChatRole.MANAGER, Invite.YES);
        chatGroupRepository.save(chatGroup);
    }

    //채팅룸 조회
    public ChatRoomResponseDto find(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id).orElseThrow();
        return new ChatRoomResponseDto(chatRoom);
    }

    @Transactional
    public void edit(ChatRoomRequestDto chatRoomRequestDto, User user) {
        Optional<ChatGroup> findChatGroup = chatGroupRepository.findByChatRoom_IdAndAndUser(chatRoomRequestDto.getId(), user);
        if (findChatGroup.isPresent()) {
            ChatRoom findChatRoom = chatRoomRepository.findById(chatRoomRequestDto.getId()).orElseThrow();
            findChatRoom.update(chatRoomRequestDto);
        } else {

        }
    }

    //채팅 초대
    public void invite(ChatRoomInviteRequestDTo chatRoomInviteRequestDTo, User user) {
        // 채팅방 번호와 유저로 내가 속한 채팅그룹을 찾음
        Optional<ChatGroup> findChatGroup = chatGroupRepository.findByChatRoom_IdAndAndUser(chatRoomInviteRequestDTo.getId(), user);
        ChatGroup chatGroup = findChatGroup.orElseThrow();

        String username = chatRoomInviteRequestDTo.getUsername(); // 초대할 유저 이름을 찾음
        User inviteUser = userRepository.findByUsername(username).orElseThrow();

        // 초대할 유저가 이미 초대되었는지 확인
        Optional<ChatGroup> findInviteUser = chatGroupRepository.findByChatRoom_IdAndAndUser(chatRoomInviteRequestDTo.getId(), inviteUser);

        // 채팅그룹에 권한을 조회해서, 내가 MANAGER이면 초대 권한을 가짐
        if (chatGroup.getChatRole().equals(ChatRole.MANAGER)) {
            if (!findInviteUser.isPresent()) {
                ChatGroup chatGroup1 = new ChatGroup(inviteUser, chatGroup.getChatRoom(), ChatRole.MEMBER, Invite.WAIT);
                chatGroupRepository.save(chatGroup1);
            }
        }
    }

    //초대 수락
    @Transactional
    public void inviteAccept(Long id, User user) {
        Optional<ChatGroup> findChatGroup = chatGroupRepository.findByChatRoom_IdAndAndUser(id, user);
        if (findChatGroup.isPresent()) {
            findChatGroup.get().setInvite(Invite.YES);
        }
    }

    //초대 거절
    @Transactional
    public void inviteDecline(Long id, User user) {
        Optional<ChatGroup> findChatGroup = chatGroupRepository.findByChatRoom_IdAndAndUser(id, user);
        if (findChatGroup.isPresent()) {
            chatGroupRepository.delete(findChatGroup.get());
        }
    }

    //채팅방 명단 : YES, INVITE
    public List<ChatUserResponseDto> findUser(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id).orElseThrow();
        List<ChatGroup> findChatGroup = chatGroupRepository.findAllByChatRoom(chatRoom);


        //유저이름, 초대여부(승낙, 대기)
        List<ChatUserResponseDto> ChatUsers = findChatGroup.stream()
                .map(ChatUserResponseDto::new)
                .collect(Collectors.toList());

        return ChatUsers;

    }

    //채팅멤버 삭제
    @Transactional
    public String delete(Long chatGroupId, User user) {
        // 내가 매니저이거나, 아니면 나 자신이 경우 삭제 가능
        ChatGroup findChatGroup = chatGroupRepository.findById(chatGroupId).get();
        ChatRoom chatRoom = findChatGroup.getChatRoom();

        // 내가 매니저인지 확인
        ChatGroup chatGroup = chatGroupRepository.findByChatRoom_IdAndAndUser(chatRoom.getId(), user).get();
        if (chatGroup.getChatRole().equals(ChatRole.MANAGER)) {
            chatGroupRepository.deleteById(chatGroupId);
            chatRoom.setChatUrl(UUID.randomUUID());
            return "삭제되었습니다.";
        }

        //내가 나 자신을 삭제하는지 확인
        if(findChatGroup.getUser().getUserId() == user.getUserId()) {
            chatGroupRepository.deleteById(chatGroupId);
            return "삭제되었습니다.";
        } else {
            return "권한이 없습니다.";
        }

    }

    @Transactional
    //채팅방 삭제
    public void deleteChatRoom(Long id, User user) {
        Optional<ChatGroup> findChatGroup = chatGroupRepository.findByChatRoom_IdAndAndUser(id, user);
        ChatGroup chatGroup = findChatGroup.get();
        if (findChatGroup.isPresent() && (chatGroup.getChatRole().equals(ChatRole.MANAGER))) {
            chatRoomRepository.delete(findChatGroup.get().getChatRoom());
        } else {
            throw new IllegalArgumentException("삭제가 실패했습니다.");
        }
    }

}
