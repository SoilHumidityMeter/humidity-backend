package com.soilhumidity.backend.service;

import com.soilhumidity.backend.dto.PageFilter;
import com.soilhumidity.backend.dto.Response;
import com.soilhumidity.backend.dto.generic.UserCreateRequest;
import com.soilhumidity.backend.dto.message.BasicResponse;
import com.soilhumidity.backend.dto.user.UserDto;
import com.soilhumidity.backend.dto.user.UserUpdateRequest;
import com.soilhumidity.backend.enums.EErrorCode;
import com.soilhumidity.backend.factory.UserFactory;
import com.soilhumidity.backend.mapper.UserMapper;
import com.soilhumidity.backend.model.User;
import com.soilhumidity.backend.repository.UserRepository;
import com.soilhumidity.backend.specs.factory.UserSpecFactory;
import com.soilhumidity.backend.validator.UserValidator;
import lombok.AllArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final UserFactory userFactory;

    private final UserValidator userValidator;

    private final MessageSourceAccessor messageSource;

    private final UserSpecFactory userSpecFactory;

    @Transactional(readOnly = true)
    public Response<Page<UserDto>> getUsers(Specification<User> specs, PageFilter pageFilter) {

        return Response.ok(
                userRepository.findAll(userSpecFactory.isActive(specs), pageFilter.asPageable()).map(userMapper::map)
        );
    }

    @Transactional
    public Response<UserDto> createUser(UserCreateRequest body) {

        var validation = userValidator.validate(body);

        if (validation.isNotValid()) {
            return Response.notOk(validation.getMessage(), EErrorCode.BAD_REQUEST);
        }

        var user = userFactory.createUser(body);

        var saved = userRepository.save(user);

        return Response.ok(userMapper.map(saved));

    }

    @Transactional
    public Response<UserDto> updateUser(Long id, UserUpdateRequest body) {

        var validation = userValidator.validate(id, body);

        if (validation.isNotValid()) {
            return Response.notOk(validation.getMessage(), EErrorCode.BAD_REQUEST);
        }

        var user = userRepository.getById(id);

        user.setName(body.getName());
        user.setSurname(body.getSurname());
        user.setPhoneNumber(body.getPhone());
        user.setEmail(body.getEmail());

        var saved = userRepository.save(user);

        return Response.ok(userMapper.map(saved));

    }

    @Transactional
    public Response<BasicResponse> deleteUser(Long id) {

        var validation = userValidator.validate(id);

        if (validation.isNotValid()) {
            return Response.notOk(validation.getMessage(), EErrorCode.BAD_REQUEST);
        }
        //Soft delete

        var user = userRepository.getById(id);
        

        user.disable();
        userRepository.save(user);

        return Response.ok(BasicResponse.of(
                messageSource.getMessage("delete_request.user.success")
        ));

    }
}
