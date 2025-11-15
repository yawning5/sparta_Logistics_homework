package com.keepgoing.order.domain.vo;

import java.util.Objects;
import java.util.UUID;

public record Hub(
    UUID hubId
) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Hub hub = (Hub) o;
        return Objects.equals(hubId, hub.hubId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(hubId);
    }
}
