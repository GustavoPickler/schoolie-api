package com.api.users.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
public class Responsible extends User {
    private String document;
}
