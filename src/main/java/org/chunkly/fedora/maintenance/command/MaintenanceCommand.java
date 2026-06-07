package org.chunkly.fedora.maintenance.command;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.chunkly.fedora.lib.Element;
import org.chunkly.fedora.lib.command.annotation.Command;
import org.chunkly.fedora.lib.command.annotation.Parameter;
import org.chunkly.fedora.lib.command.annotation.SubCommand;
import org.chunkly.fedora.maintenance.MaintenanceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MaintenanceCommand extends Element<MaintenanceManager> {

    public MaintenanceCommand(MaintenanceManager manager) {
        super(manager);
    }

    @Command(
            label = "maintenance",
            permission = "fedora.command.maintenance"
    )
    public void maintenance(ProxiedPlayer player) {
        getManager().sendMessage(player, List.of(
                "&9&m------------------------",
                "&c/maintenance toggle",
                "&c/maintenance status",
                "&c/maintenance add <player>",
                "&c/maintenance remove <player>",
                "&c/maintenance list",
                "&9&m------------------------"
        ));
    }

    @SubCommand(
            label = "toggle",
            permission = "fedora.command.maintenance.toggle",
            parent = "maintenance"
    )
    public void toggle(ProxiedPlayer player) {
        getManager().setEnabled(!getManager().isEnabled());

        try {
            getInstance().getConfigManager().setMaintenanceStatus(getManager().isEnabled());
        } catch (Exception e) {
            e.printStackTrace();
        }

        getManager().sendMessage(player,
                getManager().isEnabled()
                        ? "&aThe maintenance is now enabled."
                        : "&cThe maintenance is now disabled."
        );
    }

    @SubCommand(
            label = "status",
            permission = "fedora.command.maintenance",
            parent = "maintenance"
    )
    public void status(ProxiedPlayer player) {
        getManager().sendMessage(player,
                "&7Maintenance: " + (getManager().isEnabled() ? "&aEnabled" : "&cDisabled")
        );
    }

    @SubCommand(
            label = "add",
            permission = "fedora.command.maintenance.add",
            parent = "maintenance"
    )
    public void add(ProxiedPlayer player, @Parameter(name = "player") String playerName) {
        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(playerName);

        if (target == null) {
            getManager().sendMessage(player, "&cThat player is not online.");
            return;
        }

        if (getManager().isAccepted(target.getUniqueId())) {
            getManager().sendMessage(player, "&cThat player is already whitelisted.");
            return;
        }

        getManager().addAccepted(target.getUniqueId());
        getManager().sendMessage(player, "&aThe player &f" + target.getName() + " &ahas been added to the whitelist.");
    }

    @SubCommand(
            label = "remove",
            permission = "fedora.command.maintenance.remove",
            parent = "maintenance"
    )
    public void remove(ProxiedPlayer player, @Parameter(name = "player") String playerName) {
        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(playerName);

        if (target == null) {
            getManager().sendMessage(player, "&cThat player is not online.");
            return;
        }

        if (!getManager().isAccepted(target.getUniqueId())) {
            getManager().sendMessage(player, "&cThat player is not whitelisted.");
            return;
        }

        getManager().removeAccepted(target.getUniqueId());
        getManager().sendMessage(player, "&cThe player &f" + target.getName() + " &chas been removed from the whitelist.");
    }

    @SubCommand(
            label = "list",
            permission = "fedora.command.maintenance.list",
            parent = "maintenance"
    )
    public void list(ProxiedPlayer player) {
        var accepted = getManager().getAcceptedPlayers();

        if (accepted.isEmpty()) {
            getManager().sendMessage(player, "&eThe whitelist is empty.");
            return;
        }

        List<String> lines = new ArrayList<>();
        lines.add("&9&m------------------------");
        lines.add("&7Whitelisted players &8(&f" + accepted.size() + "&8):");

        accepted.forEach(uuid -> {
            ProxiedPlayer online = ProxyServer.getInstance().getPlayer(uuid);
            String name = online != null ? "&a" + online.getName() : "&7" + uuid;
            lines.add("  &8• " + name);
        });

        lines.add("&9&m------------------------");
        getManager().sendMessage(player, lines);
    }
}