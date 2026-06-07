package org.chunkly.fedora.lib.command.converter.impl;

import net.md_5.bungee.api.CommandSender;
import org.chunkly.fedora.lib.command.converter.IConverter;

import java.util.Collections;
import java.util.List;

public class LongConverter implements IConverter<Long> {
    @Override
    public Long fromString(String string, CommandSender sender) {
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException exception) {
            return -1L;
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender) {
        return Collections.emptyList();
    }
}
