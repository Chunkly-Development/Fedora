package org.chunkly.fedora.motd.listener;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.event.EventHandler;
import org.chunkly.fedora.lib.CC;
import org.chunkly.fedora.lib.Element;
import org.chunkly.fedora.motd.MOTDManager;

public class MOTDListener extends Element<MOTDManager> {

    public MOTDListener(MOTDManager manager) {
        super(manager);
    }


    @EventHandler
    public void onPing(ProxyPingEvent event){
        if (getInstance().getMaintenanceManager().isEnabled()){
            return;
        }

        var ping = event.getResponse();

        if (ping == null){
            return;
        }

        ping.setDescriptionComponent(
                new TextComponent(
                        CC.t(getManager().getLine1() + "\n" + getManager().getLine2())
                )
        );

        event.setResponse(ping);
    }
}
