package com.example.swithme.controller.view;

import com.example.swithme.dto.group.BoardResponseDto;
import com.example.swithme.entity.User;
import com.example.swithme.repository.UserRepository;
import com.example.swithme.security.UserDetailsImpl;
import com.example.swithme.service.GroupService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/view")
@AllArgsConstructor
public class GroupViewController {

    private final GroupService groupService;
    private final UserRepository userRepository;

    @GetMapping("/creategroup")
    public String CreateGroup() {
        return "groupstudy/groupstudywrite";
    }

    @GetMapping("/groups")
    public String ViewGroups() {
        return "groupstudy/groupstudy";
    }

    @GetMapping("/group/{groupId}/update")
    public String groupUpdate(@PathVariable Long groupId, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) throws JsonProcessingException {
        model.addAttribute("info_groupName", userDetails.getUser().getNickname());
        model.addAttribute("info_groupId", groupService.getBoard(groupId).getId());
        return "groupstudy/groupstudyupdate";
    }

    @GetMapping("/group-page/{groupId}")
    public String getPost(@PathVariable Long groupId, Long userId, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BoardResponseDto responseDto = groupService.getBoard(groupId);
        model.addAttribute("group", responseDto);
        model.addAttribute("info_userId", userDetails.getUser().getUserId());
        model.addAttribute("info_groupId", groupService.getBoard(groupId).getId());
        model.addAttribute("info_username", userDetails.getUser().getUsername());
        model.addAttribute("info_accumulatedTime", userDetails.getUser().getAccumulatedTime());
        model.addAttribute("info_groupName", groupService.getBoard(groupId).getGroupName());
        model.addAttribute("info_description", groupService.getBoard(groupId).getDescription());
        List<User> userList = userRepository.findAll();
        model.addAttribute("users", userList);

        return "groupstudy/groupstudydetails"; // postDetail.html view
    }
}
