package com.api.users.utils;

import com.api.users.model.User;
import com.api.users.repository.UserRepository;
import lombok.Getter;

@Getter
public class ValidationUtils {

    private ValidationUtils() {
        throw new AssertionError("This class should not be instantiated.");
    }

    public static UserFieldValidationResult userExists(User user, UserRepository repository) {
        String normalizedEmail = StringUtil.normalizeText(user.getEmail());

        if (repository.existsByEmail(normalizedEmail)) {
            return new UserFieldValidationResult(true, ErrorCode.EMAIL_ALREADY_REGISTERED);
        }

        if (repository.existsByPhone(user.getPhone())) {
            return new UserFieldValidationResult(true, ErrorCode.PHONE_ALREADY_REGISTERED);
        }

        return new UserFieldValidationResult(false, null);
    }
}
