package com.soilhumidity.backend.util;

import com.soilhumidity.backend.enums.ELocale;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Component
public class SoilBackendLocaleResolver extends SessionLocaleResolver {
    @NotNull
    @Override
    public Locale resolveLocale(@NotNull HttpServletRequest request) {
        return ELocale.getProperLocale(request.getLocale());
    }
}
