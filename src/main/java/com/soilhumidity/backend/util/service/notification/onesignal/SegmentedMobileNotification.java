package com.soilhumidity.backend.util.service.notification.onesignal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.soilhumidity.backend.enums.ESegment;
import com.soilhumidity.backend.util.service.notification.helper.MultiLanguageText;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public class SegmentedMobileNotification extends MobileNotification {
    @JsonProperty("included_segments")
    private final Set<ESegment> includeSegments;

    public SegmentedMobileNotification(@NotNull Set<ESegment> includeSegments,
                                       @NotNull MultiLanguageText contents,
                                       @NotNull MultiLanguageText headings) {
        super(contents, headings);
        this.includeSegments = Set.copyOf(includeSegments);
    }

    @Override
    public Set<String> getRecipient() {
        return includeSegments.stream().map(ESegment::serialize).collect(Collectors.toSet());
    }
}
