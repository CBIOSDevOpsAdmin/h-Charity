package com.himanism.hcharityapi.dto.request;

import java.util.Set;

import com.himanism.hcharityapi.entities.Role;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserReqDto {
    private Long id;
    private String fullname;
    private String username;
    private String email;
    private String mobile;
    private String role;
    private String password;
}
