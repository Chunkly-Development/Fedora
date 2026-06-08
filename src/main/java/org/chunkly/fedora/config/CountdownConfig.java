package org.chunkly.fedora.config;

public record CountdownConfig(
        boolean enabled,
        long targetEpoch,
        String finishedLine,
        String format
) {
}
