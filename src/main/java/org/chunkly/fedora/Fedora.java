package org.chunkly.fedora;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import org.chunkly.fedora.config.ConfigManager;
import org.chunkly.fedora.countdown.CountdownManager;
import org.chunkly.fedora.lib.Manager;
import org.chunkly.fedora.lib.command.CommandHandler;
import org.chunkly.fedora.maintenance.MaintenanceManager;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class Fedora extends Plugin {

    private List<Manager> managers;
    private CommandHandler commandHandler;
    private ConfigManager configManager;
    private MaintenanceManager maintenanceManager;
    private CountdownManager countdownManager;

    @Override
    public void onEnable() {
        this.managers = new ArrayList<>();
        this.commandHandler = new CommandHandler(this);
        this.configManager = new ConfigManager(this);
        this.maintenanceManager = new MaintenanceManager(this);
        this.countdownManager = new CountdownManager(this);


        this.managers.forEach(Manager::load);
    }

    @Override
    public void onDisable() {
        this.managers.forEach(Manager::unload);
    }
}
