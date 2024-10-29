package com.himanism.hcharityapi.dto.response;

import java.util.Set;

import com.himanism.hcharityapi.entities.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResDto {
    private Long id;
    private String username;
    private String email;
    private String mobile;
    private String password;
    private Set<Role> roles;
}
