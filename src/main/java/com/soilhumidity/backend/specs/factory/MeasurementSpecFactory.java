package com.soilhumidity.backend.specs.factory;

import com.soilhumidity.backend.model.Measurement;
import com.soilhumidity.backend.model.Measurement_;
import com.soilhumidity.backend.model.UserDevice_;
import com.soilhumidity.backend.model.User_;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class MeasurementSpecFactory {

    public Specification<Measurement> getByUserId(Long userId) {
        return (root, query, cb) ->
                cb.equal(root.get(Measurement_.userDevice).get(UserDevice_.user).get(User_.id), userId);
    }
}
