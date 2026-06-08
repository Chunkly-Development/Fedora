package org.chunkly.fedora.motd;

import org.chunkly.fedora.Fedora;
import org.chunkly.fedora.config.MOTDConfig;
import org.chunkly.fedora.lib.Manager;

public class MOTDManager extends Manager {

    private MOTDConfig config;

    public MOTDManager(Fedora instance) {
        super(instance);
    }

    @Override
    public void load() {
        this.config = getInstance().getConfigManager().getConfig().motd();
    }


    public String getLine1(){
        return this.config.motdLine1()
                .replace(
                        getInstance().getConfigManager().getConfig().countdown().format(),
                        getInstance().getCountdownManager().getFormattedCountdown()
                );
    }

    public String getLine2(){
        return this.config.motdLine2()
                .replace(
                        getInstance().getConfigManager().getConfig().countdown().format(),
                        getInstance().getCountdownManager().getFormattedCountdown()
                );
    }
}
