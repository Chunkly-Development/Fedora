package org.chunkly.fedora.lib;

import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.chunkly.fedora.Fedora;
import org.chunkly.fedora.lib.command.annotation.Command;
import org.chunkly.fedora.lib.command.annotation.SubCommand;

import java.lang.reflect.Method;

@Getter
public class Element<T extends Manager> implements Listener {

    private final T manager;
    private final Fedora instance;

    public Element(T manager) {
        this.manager = manager;
        this.instance = manager.getInstance();

        if (verifyCommand()) getInstance().getCommandHandler().registerCommand(this);
        if (verifyListener()) ProxyServer.getInstance().getPluginManager().registerListener(getInstance(), this);
    }

    private boolean verifyCommand(){
        for (Method method : this.getClass().getMethods()) {
            if (method.isAnnotationPresent(Command.class) || method.isAnnotationPresent(SubCommand.class)) return true;
        }
        return false;
    }

    private boolean verifyListener(){
        for (Method method : this.getClass().getMethods()) {
            if (method.isAnnotationPresent(EventHandler.class)) return true;
        }

        return false;
    }
}
