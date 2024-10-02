package com.himanism.hcharityapi.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserReqDto {
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private String mobile;
    private String role;
    private String password;
}
