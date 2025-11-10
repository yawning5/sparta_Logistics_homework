package com.keepgoing.hub.application.command;

public record CreateHubCommand(
        String name,
        String address,
        Double latitude,
        Double longitude,
        String hubStatus
) {
    public void validate() {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("허브명은 필수입니다.");
        if (address == null || address.isBlank()) throw new IllegalArgumentException("주소는 필수입니다.");
        if (latitude == null || longitude == null) throw new IllegalArgumentException("좌표는 필수입니다.");
        if (hubStatus == null || hubStatus.isBlank()) throw new IllegalArgumentException("허브 상태는 필수입니다.");
    }
}
