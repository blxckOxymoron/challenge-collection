package de.blxckoxymoron.minigames.pvearena.commands;

import de.blxckoxymoron.minigames.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;

import java.util.List;

public class ArenaCommand implements TabExecutor {

    /*
    Missing Argument for <something>
    Usage: /ask <something>
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
        return null;
    }
}
