package com.api.classes.model;

import com.api.classes.model.response.ClassInfo;
import jakarta.persistence.*;
import lombok.Data;

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

    public ClassInfo toClassInfo() {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setId(this.id);
        classInfo.setName(this.name);
        classInfo.setDescription(this.description);
        classInfo.setPassword(this.password);
        return classInfo;
    }

}
