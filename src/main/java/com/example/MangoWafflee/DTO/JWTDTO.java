package com.example.MangoWafflee.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class JWTDTO {
    private String token;
    private UserDTO user;
}

