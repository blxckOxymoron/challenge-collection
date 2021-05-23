package de.blxckoxymoron.minigames.utils;

import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

public class CommandUtils {

    public static void setTabExecutor(PluginCommand command, TabExecutor executor) {
        command.setExecutor(executor);
        command.setTabCompleter(executor);
    }
}
