package com.api.classes.service;

import com.api.classes.repository.ClassRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Slf4j
@Service
@RequiredArgsConstructor
public class CodeGeneratorService {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final ClassRepository classRepository;

    public String generateUniqueCode() {
        Random random = new Random();
        String generatedCode = generateRandomCode(random);

        while (codeExists(generatedCode)) {
            generatedCode = generateRandomCode(random);
        }

        return generatedCode;
    }

    private String generateRandomCode(Random random) {
        return IntStream.range(0, 5)
                .mapToObj(i -> CodeGeneratorService.CHARACTERS.charAt(random.nextInt(CodeGeneratorService.CHARACTERS.length())))
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    private boolean codeExists(String codeToCheck) {
        return classRepository.existsByCode(codeToCheck);
    }
}