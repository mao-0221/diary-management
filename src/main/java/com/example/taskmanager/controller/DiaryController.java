package com.example.taskmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String addOrUpdateDiary(@ModelAttribute Diary diary, Authentication auth) {
        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username);

        diary.setUser(user);

        // 既存のIDがある場合 → 編集モード
        if (diary.getId() != null) {
            Diary existingDiary = diaryRepository.findById(diary.getId()).orElse(null);
            if (existingDiary != null && existingDiary.getUser().getUsername().equals(username)) {
                // createdAt を維持する
                diary.setCreatedAt(existingDiary.getCreatedAt());
            }
        }

        // 保存（新規または編集）
        diaryRepository.save(diary);
        return "redirect:/diary";
    }

    
 // 編集用データ取得
    @GetMapping("/diary/edit/{id}")
    public String editDiary(@PathVariable Long id, Model model, Authentication auth) {
        Diary diary = diaryRepository.findById(id).orElseThrow();
        String username = ((UserDetails) auth.getPrincipal()).getUsername();

        if (!diary.getUser().getUsername().equals(username)) {
            return "redirect:/diary"; // 他人の投稿は編集させない
        }

        List<Diary> diaryList = diaryRepository.findByUser(diary.getUser());
        model.addAttribute("diaries", diaryList);
        model.addAttribute("diary", diary);
        return "diary";
    }

    // 削除
    @PostMapping("/diary/delete/{id}")
    public String deleteDiary(@PathVariable Long id, Authentication auth) {
        Diary diary = diaryRepository.findById(id).orElseThrow();
        String username = ((UserDetails) auth.getPrincipal()).getUsername();

        if (diary.getUser().getUsername().equals(username)) {
            diaryRepository.delete(diary);
        }

        return "redirect:/diary";
    }
}

