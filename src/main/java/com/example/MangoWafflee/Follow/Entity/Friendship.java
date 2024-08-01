package com.example.MangoWafflee.Follow.Entity;

import com.example.MangoWafflee.Entity.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user1_id")
    private UserEntity user1;

    @ManyToOne
    @JoinColumn(name = "user2_id")
    private UserEntity user2;

    private LocalDateTime friendshipDate;
}
