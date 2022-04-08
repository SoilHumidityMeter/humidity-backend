package com.soilhumidity.backend.specs.operators;

import com.soilhumidity.backend.enums.ERoleType;
import net.kaczmarzyk.spring.data.jpa.domain.PathSpecification;
import net.kaczmarzyk.spring.data.jpa.utils.QueryContext;
import org.jetbrains.annotations.NotNull;

import javax.persistence.criteria.*;
import java.util.Arrays;

public class InRole<T> extends PathSpecification<T> {

    private static final long serialVersionUID = 1L;
    protected ERoleType role;

    public InRole(QueryContext queryContext, String path, String... args) {
        super(queryContext, path);
        if (args != null && args.length == 1) {
            role = ERoleType.valueOf(args[0]);
        } else {
            throw new IllegalArgumentException("Expected exactly one argument (the fragment to match against), but got: " + Arrays.toString(args));
        }
    }

    @Override
    public Predicate toPredicate(@NotNull Root<T> root, @NotNull CriteriaQuery<?> criteriaQuery, @NotNull CriteriaBuilder criteriaBuilder) {
        Path<Object> rolePath = path(root);

        return criteriaBuilder.equal(rolePath, ERoleType.getRoleValue(role));
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (role == null ? 0 : role.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!super.equals(obj)) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        } else {
            InRole<?> other = (InRole<?>) obj;
            if (role == null) {
                return other.role == null;
            } else {
                return role.equals(other.role);
            }
        }
    }

    @Override
    public String toString() {
        return "InRole [role=" + role + "]";
    }
}
