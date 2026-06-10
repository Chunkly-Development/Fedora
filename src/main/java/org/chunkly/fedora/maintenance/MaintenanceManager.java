package org.chunkly.fedora.maintenance;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.chunkly.fedora.Fedora;
import org.chunkly.fedora.lib.Manager;
import org.chunkly.fedora.maintenance.command.MaintenanceCommand;
import org.chunkly.fedora.maintenance.listener.MaintenanceListener;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MaintenanceManager extends Manager {

    private final Set<UUID> acceptedPlayers;
    private boolean status;

    public MaintenanceManager(Fedora instance) {
        super(instance);
        this.acceptedPlayers = new HashSet<>();
        this.status = false;

        new MaintenanceListener(this);
        new MaintenanceCommand(this);
    }

    @Override
    public void load() {
        this.status = getInstance().getConfigManager().getMaintenance().status();

        try {
            acceptedPlayers.addAll(
                    getInstance().getConfigManager().getAcceptedPlayersFromConfig()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unload() {
        try{
            getInstance().getConfigManager().setMaintenanceStatus(status);
            getInstance().getConfigManager().saveAcceptedPlayers(acceptedPlayers);
        }catch (IOException e){
            e.fillInStackTrace();
        }
    }

    public boolean isEnabled() {
        return status;
    }

    public void setEnabled(boolean status) {
        this.status = status;
    }

    public boolean isAccepted(ProxiedPlayer player) {
        return acceptedPlayers.contains(player.getUniqueId());
    }

    public boolean isAccepted(UUID uuid) {
        return acceptedPlayers.contains(uuid);
    }

    public void addAccepted(UUID uuid) {
        acceptedPlayers.add(uuid);
    }

    public void removeAccepted(UUID uuid) {
        acceptedPlayers.remove(uuid);
    }

    public Set<UUID> getAcceptedPlayers() {
        return java.util.Collections.unmodifiableSet(acceptedPlayers);
    }
}