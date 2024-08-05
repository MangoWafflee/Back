package com.example.MangoWafflee.Follow.Entity;

import com.example.MangoWafflee.Entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class FollowRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="sender_id")
    private UserEntity sender;

    @ManyToOne
    @JoinColumn(name="receiver_id")
    private UserEntity receiver;

    @Enumerated(EnumType.STRING)
    private FollowRequestStatus status;

    private LocalDateTime requestDate;
}