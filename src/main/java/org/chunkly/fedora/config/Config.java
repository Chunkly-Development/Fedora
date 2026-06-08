package org.chunkly.fedora.config;

public record Config(
        MaintenanceConfig maintenance,
        CountdownConfig countdown,
        MOTDConfig motd
) {
}
