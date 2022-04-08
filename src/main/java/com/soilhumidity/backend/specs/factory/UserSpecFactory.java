package com.soilhumidity.backend.specs.factory;

import com.soilhumidity.backend.model.User_;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import com.soilhumidity.backend.model.User;

@Component
@NoArgsConstructor
public class UserSpecFactory {

    public Specification<User> isActive(Specification<User> specifications) {
        Specification<User> isActive = (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.isFalse(root.get(User_.disabled));

        if (specifications == null) {
            return isActive;
        }
        return isActive.and(specifications);
    }
}
