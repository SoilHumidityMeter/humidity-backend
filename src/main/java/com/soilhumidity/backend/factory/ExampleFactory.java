package com.soilhumidity.backend.factory;

import com.soilhumidity.backend.dto.generic.UserCreateRequest;
import com.soilhumidity.backend.model.User;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@NoArgsConstructor
public class ExampleFactory {

    public Example<User> createUser(@NotNull UserCreateRequest body) {
        var user = new User();

        user.setEmail(body.getEmail().toLowerCase(Locale.US));
        user.setPhoneNumber(body.getPhone());

        return Example.of(user, ExampleMatcher.matchingAny()
                .withIgnorePaths("id", "password", "role", "name", "surname", "confirmed",
                        "created_on", "banned", "profile_picture").withIgnoreNullValues());
    }

}
