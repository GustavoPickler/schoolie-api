package com.api.users.model.response;

import com.api.users.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private UserDTO user;
    private String message;
    private int code;

    public UserResponse(UserDTO user, String message, int code) {
        this.user = user;
        this.message = message;
        this.code = code;
    }
}