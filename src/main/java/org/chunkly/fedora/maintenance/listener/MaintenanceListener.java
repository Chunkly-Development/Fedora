package org.chunkly.fedora.maintenance.listener;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.chunkly.fedora.lib.CC;
import org.chunkly.fedora.lib.Element;
import org.chunkly.fedora.maintenance.MaintenanceManager;

public class MaintenanceListener extends Element<MaintenanceManager> {

    public MaintenanceListener(MaintenanceManager manager) {
        super(manager);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPing(ProxyPingEvent event) {
        if (!getManager().isEnabled()){
            return;
        }

        var ping = event.getResponse();

        if (ping == null){
            return;
        }

        ping.setVersion(new ServerPing.Protocol(
                CC.t("&4Maintenance"),
                -1
        ));

        ping.setDescriptionComponent(new TextComponent(
                CC.t(getInstance().getConfigManager().getMaintenance().motdLine1() + "\n" + getInstance().getConfigManager().getMaintenance().motdLine2())
        ));

        event.setResponse(ping);
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        if (!getManager().isEnabled()){
            return;
        }

        if (getManager().isAccepted(event.getConnection().getUniqueId())){
            return;
        }

        event.setReason(new TextComponent(
            CC.t(getInstance().getConfigManager().getConfig().maintenance().kickReason())
        ));

        event.setCancelled(true);
    }
}
