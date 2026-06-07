package org.chunkly.fedora.lib.command.converter.impl;

import net.md_5.bungee.api.CommandSender;
import org.chunkly.fedora.lib.command.converter.IConverter;

import java.util.Collections;
import java.util.List;

public class BooleanConverter implements IConverter<Boolean> {
    @Override
    public Boolean fromString(String string, CommandSender sender) {
        try {
            return Boolean.parseBoolean(string);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender) {
        return Collections.emptyList();
    }
}
