package org.chunkly.fedora.countdown;

import org.chunkly.fedora.Fedora;
import org.chunkly.fedora.config.CountdownConfig;
import org.chunkly.fedora.lib.Manager;

public class CountdownManager extends Manager {

    private CountdownConfig config;

    public CountdownManager(Fedora instance) {
        super(instance);
    }

    @Override
    public void load() {
        this.config = getInstance().getConfigManager().getConfig().countdown();
    }

    public boolean isEnabled(){
        return config.enabled();
    }

    public boolean isFinished(){
        return getRemainingSeconds() <= 0;
    }

    public long getRemainingSeconds() {
        return config.targetEpoch() - (System.currentTimeMillis() / 1000L);
    }


    public String getFormattedCountdown() {
        long remaining = getRemainingSeconds();

        if (remaining <= 0) return config.finishedLine();

        long days    = remaining / 86400;
        long hours   = (remaining % 86400) / 3600;
        long minutes = (remaining % 3600) / 60;
        long seconds = remaining % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0)    sb.append(days).append("d ");
        if (hours > 0)   sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");
        sb.append(seconds).append("s");

        return sb.toString().trim();
    }



}
