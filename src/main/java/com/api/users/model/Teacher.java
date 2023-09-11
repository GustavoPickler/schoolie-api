package com.api.users.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
public class Teacher extends User {
    private String document;
}
