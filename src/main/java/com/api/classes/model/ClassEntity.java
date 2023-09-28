package com.api.classes.model;

import com.api.users.model.Teacher;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "classes")
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String code;

    @OneToOne
    @JoinColumn(name = "owner_id")
    private Teacher owner;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "register_date")
    private Date registerDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update")
    private Date lastUpdate;

    @PrePersist
    protected void onCreate() {
        registerDate = new Date();
        lastUpdate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdate = new Date();
    }

}
