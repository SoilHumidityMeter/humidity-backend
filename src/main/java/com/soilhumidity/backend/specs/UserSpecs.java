package com.soilhumidity.backend.specs;

import com.soilhumidity.backend.specs.operators.InRole;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Conjunction;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;
import com.soilhumidity.backend.model.User;

@Conjunction(value = {
        @Or({
                @Spec(path = "fullName", params = "search", spec = LikeIgnoreCase.class),
                @Spec(path = "email", params = "search", spec = LikeIgnoreCase.class),
                @Spec(path = "phoneNumber", params = "search", spec = LikeIgnoreCase.class),
                @Spec(path = "profilePicture", params = "search", spec = LikeIgnoreCase.class)
        })},
        and = {
                @Spec(path = "id", params = "user", spec = Equal.class),
                @Spec(path = "role", params = "role", spec = InRole.class),
                @Spec(path = "disabled", defaultVal = "false", spec = Equal.class)
        }
)
public interface UserSpecs extends Specification<User> {
}
