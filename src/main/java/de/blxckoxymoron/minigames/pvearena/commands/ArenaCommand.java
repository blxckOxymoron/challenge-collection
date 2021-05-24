package de.blxckoxymoron.minigames.pvearena.commands;

import de.blxckoxymoron.minigames.utils.ChatUtils;
import de.blxckoxymoron.minigames.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ArenaCommand implements TabExecutor {

    /*
    Missing Argument for <something>
    Usage: /ask <something>

    /arena <create|delete|teleport|edit> <name>
     */

    public ChatUtils commandChat;

    public ArenaCommand(ChatUtils commandChat) {
        this.commandChat = commandChat;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Entity)) {commandChat.sendError(sender, "You are not an Entity!"); return false;}

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        List<String> options = new ArrayList<>();

        switch (args.length) {
            case 1: {
                return CommandUtils.matchingOptions(args[0], "create", "delete", "teleport", "edit");
            }
            case 2: {
                return CommandUtils.matchingOptions(args[1], "name1", "name2");
            }
        }

        return options;

    }
}
