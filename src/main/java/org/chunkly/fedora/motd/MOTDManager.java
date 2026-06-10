package org.chunkly.fedora.motd;

import org.chunkly.fedora.Fedora;
import org.chunkly.fedora.lib.Manager;
import org.chunkly.fedora.motd.command.MOTDCommand;
import org.chunkly.fedora.motd.listener.MOTDListener;

public class MOTDManager extends Manager {

    public MOTDManager(Fedora instance) {
        super(instance);
    }

    @Override
    public void load() {
        new MOTDListener(this);
        new MOTDCommand(this);
    }

    public String getLine1(){
        return getInstance().getConfigManager().getConfig().motd().motdLine1()
                .replace(
                        getInstance().getConfigManager().getConfig().countdown().format(),
                        getInstance().getCountdownManager().getFormattedCountdown()
                );
    }

    public String getLine2(){
        return getInstance().getConfigManager().getConfig().motd().motdLine2()
                .replace(
                        getInstance().getConfigManager().getConfig().countdown().format(),
                        getInstance().getCountdownManager().getFormattedCountdown()
                );
    }
}
