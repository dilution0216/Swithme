package com.example.swithme.controller;

import com.example.swithme.dto.CalendarResponseDto;
import com.example.swithme.entity.Calendar;
import com.example.swithme.entity.User;
import com.example.swithme.repository.CalendarRepository;
import com.example.swithme.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarRepository studyRepository;

    // 캘린더 작성 폼
    @GetMapping("/studies/new")
    public String showCreateForm(Model model) {
        model.addAttribute("study", new Calendar());
        return "study/createForm";
    }

    // 캘린더 일정 작성기능
    @PostMapping("/studies")
    public String createStudy(@ModelAttribute Calendar study, @AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        User user = userDetails.getUser();
        study.setUser(user);

        if(study.getStartTime().isAfter(study.getEndTime())) {
            model.addAttribute("error", "시작 시간은 종료 시간보다 이전이어야 합니다.");
            return "redirect:/studies/calendar";
        }

        studyRepository.save(study);
        return "redirect:/studies/calendar";
    }

    // 캘린더 일정조회 (템플릿)
    @GetMapping("/studies/calendar")
    public String openCalendar() {
        return "study/calendar";
    }


    //캘린더 조회출력
    @ResponseBody
    @GetMapping("/studies/calendar1")
    public List<CalendarResponseDto> showCalendar(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        List<Calendar> studies = studyRepository.findAllByUser(user);

        List<CalendarResponseDto> studiesEntity = studies.stream()
                .map(CalendarResponseDto::new)
                .collect(Collectors.toList());

        return studiesEntity;
    }

    @GetMapping("/studies/details/{id}")
    public String showStudyDetails(@PathVariable Long id, Model model) {
        Optional<Calendar> optionalStudy = studyRepository.findById(id);
        if (optionalStudy.isPresent()) {
            Calendar study = optionalStudy.get();
            model.addAttribute("study", study);
            return "study/details";
        }
        return "redirect:/studies/calendar";
    }

    @GetMapping("/studies/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Calendar> optionalStudy = studyRepository.findById(id);
        if (optionalStudy.isPresent()) {
            Calendar study = optionalStudy.get();
            model.addAttribute("study", study);
            return "study/editForm";
        }
        return "redirect:/studies/calendar";
    }

    @PostMapping("/studies/update")
    public String updateStudy(@ModelAttribute Calendar study, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        study.setUser(user);
        studyRepository.save(study);
        return "redirect:/studies/calendar";
    }

    @GetMapping("/studies/delete/{id}")
    public String deleteStudy(@PathVariable Long id) {
        studyRepository.deleteById(id);
        return "redirect:/studies/calendar";
    }
}


