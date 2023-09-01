package com.api.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseDTO {

    @JsonProperty("access_token")
    private String token;

    public LoginResponseDTO(String token) {
        this.token = token;
    }

}