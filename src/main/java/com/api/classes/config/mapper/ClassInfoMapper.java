package com.api.classes.config.mapper;

import com.api.classes.model.ClassEntity;
import com.api.classes.model.response.ClassInfo;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClassInfoMapper {

    public static ClassInfo mapToClassInfo(ClassEntity classEntity) {
        ClassInfo classInfo = new ClassInfo();
        classInfo.setId(classEntity.getId());
        classInfo.setName(classEntity.getName());
        classInfo.setDescription(classEntity.getDescription());
        classInfo.setPassword(classEntity.getPassword());

        return classInfo;
    }
}

