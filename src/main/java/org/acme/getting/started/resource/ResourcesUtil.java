package org.acme.getting.started.resource;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Collectors;

public class ResourcesUtil {

    private ResourcesUtil() {
    }

    public static String buildErrorMessageByViolations(Set<? extends ConstraintViolation<?>> violations) {
        return violations
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
    }
}
