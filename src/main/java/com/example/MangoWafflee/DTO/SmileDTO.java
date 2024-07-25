package com.example.MangoWafflee.DTO;

import com.example.MangoWafflee.Entity.SmileEntity;
import lombok.*;

import java.time.LocalDate;

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

    // DTO를 Entity로 변환하는 메소드
    public static SmileEntity toEntity(SmileDTO smileDTO) {
        return SmileEntity.builder()
                .smilePercentage(smileDTO.getSmilePercentage())
                .date(smileDTO.getDate())
                .build();
    }

    // Entity를 DTO로 변환하는 메소드
    public static SmileDTO fromEntity(SmileEntity entity) {
        return SmileDTO.builder()
                .smilePercentage(entity.getSmilePercentage())
                .date(entity.getDate())
                .nickname(entity.getUser().getNickname()) // 유저 이름을 포함
                .build();
    }
}
