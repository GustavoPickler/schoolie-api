package com.api.users.model.response;

import com.api.users.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UsersResponse {
    private List<UserDTO> users;
    private String message;
    private int code;
}