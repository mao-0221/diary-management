package com.example.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskmanager.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    // 必要ならカスタムメソッドも追加できます（例：findByCompleted(true) など）
}
