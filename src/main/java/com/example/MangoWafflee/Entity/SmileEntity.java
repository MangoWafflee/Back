package com.example.MangoWafflee.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    @ManyToOne
    @JoinColumn(name = "nickname", referencedColumnName = "nickname")
    private UserEntity user;
}