package com.example.MangoWafflee.DTO;

import com.example.MangoWafflee.Entity.SmileEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SmileDTO {
    private String nickname;
    private double smilePercentage;
    private LocalDate date;
    private LocalTime time; // 새로운 시간 필드 추가

    // DTO를 Entity로 변환하는 메소드
    public static SmileEntity toEntity(SmileDTO smileDTO) {
        return SmileEntity.builder()
                .smilePercentage(smileDTO.getSmilePercentage())
                .date(smileDTO.getDate())
                .time(smileDTO.getTime()) // 시간 정보 포함
                .build();
    }

    // Entity를 DTO로 변환하는 메소드
    public static SmileDTO fromEntity(SmileEntity entity) {
        return SmileDTO.builder()
                .smilePercentage(entity.getSmilePercentage())
                .date(entity.getDate())
                .time(entity.getTime()) // 시간 정보 포함
                .nickname(entity.getUser().getNickname()) // 유저 이름을 포함
                .build();
    }
}
