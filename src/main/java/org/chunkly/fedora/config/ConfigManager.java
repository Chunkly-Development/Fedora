package org.chunkly.fedora.config;

import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.chunkly.fedora.Fedora;
import org.chunkly.fedora.lib.Manager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ConfigManager extends Manager {

    private File configFile;
    @Getter
    private Config config;

    public ConfigManager(Fedora instance) {
        super(instance);
    }

    @Override
    public void load() {
        try {
            File dataFolder = getInstance().getDataFolder();

            if (!dataFolder.exists()) {
                Files.createDirectories(dataFolder.toPath());
            }

            this.configFile = new File(dataFolder, "config.yml");

            if (!this.configFile.exists()) {
                saveDefaultConfig(configFile);
            }

            this.config = parseConfig();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() throws IOException {
        this.config = parseConfig();
    }


    private Config parseConfig() throws IOException {
        Configuration yaml = ConfigurationProvider
                .getProvider(YamlConfiguration.class)
                .load(configFile);

        return new Config(
                parseMaintenance(yaml)
        );
    }

    private MaintenanceConfig parseMaintenance(Configuration yaml) {
        return new MaintenanceConfig(
                yaml.getBoolean("maintenance.status", false),
                yaml.getString("maintenance.kick-reason"),
                yaml.getString("maintenance.version-text"),
                yaml.getString("maintenance.motd.line-1"),
                yaml.getString("maintenance.motd.line-2")
        );
    }


    public void setMaintenanceStatus(boolean value) throws IOException {
        Configuration yaml = ConfigurationProvider
                .getProvider(YamlConfiguration.class)
                .load(configFile);

        yaml.set("maintenance.status", value);

        ConfigurationProvider
                .getProvider(YamlConfiguration.class)
                .save(yaml, configFile);

        this.config = parseConfig();
    }

    public void saveAcceptedPlayers(Set<UUID> players) throws IOException {
        Configuration yaml = ConfigurationProvider
                .getProvider(YamlConfiguration.class)
                .load(configFile);

        List<String> uuids = players.stream()
                .map(UUID::toString)
                .collect(java.util.stream.Collectors.toList());

        yaml.set("maintenance.accepted-players", uuids);

        ConfigurationProvider
                .getProvider(YamlConfiguration.class)
                .save(yaml, configFile);
    }

    public List<UUID> getAcceptedPlayersFromConfig() throws IOException {
        Configuration yaml = ConfigurationProvider
                .getProvider(YamlConfiguration.class)
                .load(configFile);

        return yaml.getStringList("maintenance.accepted-players")
                .stream()
                .map(UUID::fromString)
                .collect(java.util.stream.Collectors.toList());
    }

    public MaintenanceConfig getMaintenance() {
        return config.maintenance();
    }

    private void saveDefaultConfig(File configFile) throws IOException {
        try (InputStream inputStream = getInstance().getResourceAsStream("config.yml")) {
            if (inputStream == null) {
                throw new IOException("Default config.yml resource not found");
            }
            Files.copy(inputStream, configFile.toPath());
        }
    }
}