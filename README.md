# What is Fedora?
Besides being a Linux distribution, Fedora is a BungeeCord plugin
that contains multiple tools to manage a Minecraft server.

Designed to cover the needs of Developers, Owners, or anyone
building a network, allowing them to manage most of these needs
in a single plugin.

## Features
* Global Broadcast
* Maintenance Mode (with Whitelist)
* MOTD Countdown

## Coming Soon
* AntiVPN System
* AntiBot System

---

## Permissions

### General
| Permission | Description |
|---|---|
| `fedora.command.broadcast` | Send a broadcast to the entire network |

### Maintenance
| Permission                          | Description                          |
|-------------------------------------|--------------------------------------|
| `fedora.command.maintenance`        | View the list of subcommands         |
| `fedora.command.maintenance.toggle` | Enable or disable maintenance mode   |
| `fedora.command.maintenance.add`    | Add a player to the whitelist        |
| `fedora.command.maintenance.remove` | Remove a player from the whitelist   |
| `fedora.command.maintenance.list`   | View players in the whitelist        |

### MOTD
| Permission                            | Description                        |
|---------------------------------------|------------------------------------|
| `fedora.command.motd`                 | Manage the MOTD                    |
| `fedora.command.motd.line1`           | Edit line 1 of the MOTD            |
| `fedora.command.motd.line2`           | Edit line 2 of the MOTD            |
| `fedora.command.motd.countdown`       | View the countdown command list    |
| `fedora.command.motd.countdown.start` | Start the countdown                |
| `fedora.command.motd.countdown.stop`  | Stop the countdown                 |