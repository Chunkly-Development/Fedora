package org.chunkly.fedora.config;

public record MaintenanceConfig(
        boolean status,
        String kickReason,
        String versionText,
        String motdLine1,
        String motdLine2
) {}
