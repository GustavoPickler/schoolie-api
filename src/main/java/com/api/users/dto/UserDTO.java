package com.api.users.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String type;

    public UserDTO(Long id, String email, String type) {
        this.id = id;
        this.email = email;
        this.type = type;
    }

}