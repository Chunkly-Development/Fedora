package org.chunkly.fedora.motd.command;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.chunkly.fedora.lib.Element;

import java.util.List;
import org.chunkly.fedora.lib.command.annotation.Command;
import org.chunkly.fedora.lib.command.annotation.Optional;
import org.chunkly.fedora.lib.command.annotation.Parameter;
import org.chunkly.fedora.lib.command.annotation.SubCommand;
import org.chunkly.fedora.motd.MOTDManager;

public class MOTDCommand extends Element<MOTDManager> {

    public MOTDCommand(MOTDManager manager) {
        super(manager);
    }

    @Command(label = "motd", permission = "fedora.command.motd")
    public void motd(ProxiedPlayer player) {
        getManager().sendMessage(player, List.of(
                "&9&m------------------------",
                "&c/motd setline <1-2> <text>",
                "&c/motd countdown",
                "&9&m------------------------"
        ));
    }

    @SubCommand(label = "setline", permission = "fedora.command.motd", parent = "motd", appendStrings = true)
    public void setline(ProxiedPlayer player, @Parameter(name = "line") int line, @Parameter(name = "text") String text) {
        if (line < 1 || line > 2) {
            getManager().sendMessage(player, "&cLine must be 1 or 2.");
            return;
        }
        String perm = line == 1 ? "fedora.command.motd.line1" : "fedora.command.motd.line2";
        if (!player.hasPermission(perm)) {
            getManager().sendMessage(player, "&cNo permission.");
            return;
        }
        try {
            if (line == 1) {
                getInstance().getConfigManager().setMOTDLine1(text);
                getManager().sendMessage(player, "&aMOTD line 1 has been updated.");
                return;
            }
            getInstance().getConfigManager().setMOTDLine2(text);
            getManager().sendMessage(player, "&aMOTD line 2 has been updated.");
        } catch (Exception e) {
            getManager().sendMessage(player, "&cFailed to update MOTD line " + line + ".");
        }
    }

    @SubCommand(label = "countdown", permission = "fedora.command.motd.countdown", parent = "motd", appendStrings = true)
    public void countdown(ProxiedPlayer player, @Optional("help") String args) {
        if (args == null || args.isEmpty()) {
            args = "help";
        }
        String[] split = args.split(" ");
        if (split.length >= 1 && split[0].equalsIgnoreCase("start")) {
            if (!player.hasPermission("fedora.command.motd.countdown.start")) {
                getManager().sendMessage(player, "&cNo permission.");
                return;
            }
            if (split.length < 2) {
                getManager().sendMessage(player, "&cUsage: /motd countdown start <yyyy-MM-dd HH:mm:ss>");
                return;
            }
            StringBuilder dateBuilder = new StringBuilder();
            for (int i = 1; i < split.length; i++) {
                dateBuilder.append(split[i]).append(" ");
            }
            String date = dateBuilder.toString().trim();
            try {
                getInstance().getCountdownManager().setTarget(date);
                getInstance().getCountdownManager().setEnabled(true);
                getManager().sendMessage(player, "&aCountdown started with target: &f" + date);
            } catch (Exception e) {
                getManager().sendMessage(player, "&cInvalid date format. Use: yyyy-MM-dd HH:mm:ss");
            }
            return;
        }
        if (split.length >= 1 && split[0].equalsIgnoreCase("stop")) {
            if (!player.hasPermission("fedora.command.motd.countdown.stop")) {
                getManager().sendMessage(player, "&cNo permission.");
                return;
            }
            getInstance().getCountdownManager().setEnabled(false);
            getManager().sendMessage(player, "&cCountdown stopped.");
            return;
        }
        getManager().sendMessage(player, List.of(
                "&9&m------------------------",
                "&c/motd countdown start <yyyy-MM-dd HH:mm:ss>",
                "&c/motd countdown stop",
                "&9&m------------------------"
        ));
    }
}
