package org.chunkly.fedora.lib;

import lombok.Getter;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.chunkly.fedora.Fedora;

import java.util.Arrays;
import java.util.List;

@Getter
public class Manager {

    private final Fedora instance;

    public Manager(Fedora instance) {
        this.instance = instance;

        getInstance().getManagers().add(this);
    }


    public void sendMessage(ProxiedPlayer player, String message) {
        player.sendMessage(new TextComponent(CC.t(message)));
    }

    public void sendMessage(ProxiedPlayer player, List<String> messages) {
        messages.forEach(message -> sendMessage(player, message));
    }

    public void load(){}
    public void unload(){}
}
