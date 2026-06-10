package org.chunkly.fedora.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import org.chunkly.fedora.Fedora;
import org.chunkly.fedora.lib.CC;
import org.chunkly.fedora.lib.command.annotation.Command;
import org.chunkly.fedora.lib.command.annotation.Parameter;

public class BroadcastCommand {

    public BroadcastCommand(Fedora instance) {
        instance.getCommandHandler().registerCommand(this);
    }

    @Command(label = "broadcast", permission = "fedora.command.broadcast", aliases = {"bc"}, appendStrings = true)
    public void broadcast(CommandSender sender, @Parameter(name = "message") String message) {
        ProxyServer.getInstance().broadcast(CC.t("&8[&4&lBC&8] &r" + message));
    }
}
