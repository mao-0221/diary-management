package com.example.taskmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.taskmanager.entity.Diary;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.DiaryRepository;
import com.example.taskmanager.repository.UserRepository;

@Controller
public class DiaryController {

    @Autowired
    private DiaryRepository diaryRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/diary")
    public String showDiary(Model model, Authentication auth) {
        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username);

        List<Diary> diaryList = diaryRepository.findByUser(user);
        model.addAttribute("diaries", diaryList);
        model.addAttribute("diary", new Diary());
        return "diary";
    }

    @PostMapping("/diary")
    public String addDiary(@ModelAttribute Diary diary, Authentication auth) {
        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username);

        diary.setUser(user);
        diaryRepository.save(diary);
        return "redirect:/diary";
    }
}
