package com.api.users.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@DiscriminatorValue("RESPONSIBLE")
public class Responsible extends User {
    private String document;
}
