package com.example.swithme.repository;

import com.example.swithme.entity.Calendar;
import com.example.swithme.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    List<Calendar> findAllByUser(User user);
}

