package com.example.MangoWafflee.Follow.DTO;

import com.example.MangoWafflee.Follow.Entity.FollowRequestStatus;
import lombok.Data;

@Data
public class FollowResponseDTO {
    private Long requestId;
    private FollowRequestStatus status;
}
