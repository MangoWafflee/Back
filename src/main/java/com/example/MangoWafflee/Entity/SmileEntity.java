package com.example.MangoWafflee.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Builder
@Table(name = "smiles")
public class SmileEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long smileId;

    @Column(nullable = false)
    private double smilePercentage;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time; // 새로운 시간 필드 추가

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;
}