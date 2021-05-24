package de.blxckoxymoron.minigames.utils;

import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

import java.util.LinkedList;
import java.util.List;

public class CommandUtils {

    public static void setTabExecutor(PluginCommand command, TabExecutor executor) {
        command.setExecutor(executor);
        command.setTabCompleter(executor);
    }

    public static List<String> matchingOptions(String arg, String... openOptions) {

        List<String> options = new LinkedList<>();

        for (String option : openOptions) {
            if (option.startsWith(arg)) {
                options.add(option);
            }
        }

        return options;
    }
}
