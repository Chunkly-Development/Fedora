package org.chunkly.fedora.lib.command.converter.impl;

import net.md_5.bungee.api.CommandSender;
import org.chunkly.fedora.lib.command.converter.IConverter;

import java.util.Collections;
import java.util.List;

public class IntegerConverter implements IConverter<Integer> {
    @Override
    public Integer fromString(String string, CommandSender sender) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender) {
        return Collections.emptyList();
    }
}
