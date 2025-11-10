package com.keepgoing.hub.application.command;

public record UpdateHubCommand(
        String name,
        String address,
        Double latitude,
        Double longitude,
        String hubStatus
) {}