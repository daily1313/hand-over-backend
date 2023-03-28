package com.example.handoverbackend.repository;

import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.domain.message.Message;
import com.example.handoverbackend.dto.message.MessageResponseDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositoryCustom {


    Page<Message> findBySender(Member sender, Pageable pageable);
    Page<Message> findByReceiver(Member receiver, Pageable pageable);
}
