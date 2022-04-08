package com.soilhumidity.backend.mapper;

import com.soilhumidity.backend.dto.user.UserDto;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import com.soilhumidity.backend.model.User;

@Component
@NoArgsConstructor
public class UserMapper {

    public UserDto map(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPhoneNumber()
        );
    }
}
