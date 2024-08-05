package com.example.MangoWafflee.Follow.DTO;

import com.example.MangoWafflee.DTO.UserDTO;
import com.example.MangoWafflee.Entity.UserEntity;
import com.example.MangoWafflee.Follow.Entity.FollowRequest;
import com.example.MangoWafflee.Follow.Entity.FollowRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowRequestDTO {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private FollowRequestStatus status;
    private LocalDateTime requestDate;

    //엔티티를 직접 반환하게 하지않고 필요한 데이터만 포함한 객체를 반환하도록 DTO를 사용
    public static FollowRequestDTO entityToDto(FollowRequest followRequest) {
        return new FollowRequestDTO(
                followRequest.getId(),
                followRequest.getSender().getId(),
                followRequest.getReceiver().getId(),
                followRequest.getStatus(),
                followRequest.getRequestDate()
        );
    }

    public FollowRequest dtoToEntity(UserEntity sender, UserEntity receiver) {
        return new FollowRequest(id, sender, receiver, status, requestDate);
    }
}
