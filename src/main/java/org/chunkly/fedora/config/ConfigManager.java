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
import java.util.stream.Collectors;

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
                parseMaintenance(yaml),
                parseCountdown(yaml),
                parseMOTDConfig(yaml)
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

    private MOTDConfig parseMOTDConfig(Configuration yaml) {
        return new MOTDConfig(
                yaml.getString("motd.line-1"),
                yaml.getString("motd.line-2")
        );
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

    private CountdownConfig parseCountdown(Configuration yaml) {
        boolean enabled = yaml.getBoolean("countdown.enabled", false);
        String finished = yaml.getString("countdown.finished-line", "&aOpen!");
        String format   = yaml.getString("countdown.format", "{countdown}");

        String target = yaml.getString("countdown.target", "2025-01-01 00:00:00");
        long epoch = parseEpoch(target);

        return new CountdownConfig(enabled, epoch, finished, format);
    }

    private long parseEpoch(String dateStr) {
        try {
            java.time.LocalDateTime dt = java.time.LocalDateTime.parse(
                    dateStr,
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            );
            return dt.toEpochSecond(java.time.ZoneOffset.UTC);
        } catch (Exception e) {
            getInstance().getLogger().warning("Invalid countdown date: " + dateStr);
            return 0L;
        }
    }

    public void setMaintenanceStatus(boolean value) throws IOException {
        updateConfig(yaml -> yaml.set("maintenance.status", value));
    }

    public void saveAcceptedPlayers(Set<UUID> players) throws IOException {
        updateConfig(yaml -> yaml.set("maintenance.accepted-players",
                players.stream().map(UUID::toString).collect(Collectors.toList())));
    }

    public void setCountdownEnabled(boolean value) throws IOException {
        updateConfig(yaml -> yaml.set("countdown.enabled", value));
    }

    public void setCountdownTarget(String date) throws IOException {
        updateConfig(yaml -> yaml.set("countdown.target", date));
    }

    public void setMOTDLine1(String motdLine1) throws IOException{
        updateConfig(yaml -> yaml.set("motd.line-1", motdLine1));
    }

    public void setMOTDLine2(String motdLine1) throws IOException{
        updateConfig(yaml -> yaml.set("motd.line-2", motdLine1));
    }

    private void saveDefaultConfig(File configFile) throws IOException {
        try (InputStream inputStream = getInstance().getResourceAsStream("config.yml")) {
            if (inputStream == null) {
                throw new IOException("Default config.yml resource not found");
            }
            Files.copy(inputStream, configFile.toPath());
        }
    }

    private void updateConfig(java.util.function.Consumer<Configuration> updater) throws IOException {
        Configuration yaml = ConfigurationProvider
                .getProvider(YamlConfiguration.class)
                .load(configFile);

        updater.accept(yaml);

        ConfigurationProvider
                .getProvider(YamlConfiguration.class)
                .save(yaml, configFile);

        this.config = parseConfig();
    }
}