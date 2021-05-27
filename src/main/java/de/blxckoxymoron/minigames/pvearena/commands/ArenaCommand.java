package de.blxckoxymoron.minigames.pvearena.commands;

import de.blxckoxymoron.minigames.Minigames;
import de.blxckoxymoron.minigames.pvearena.Arena;
import de.blxckoxymoron.minigames.utils.ChatUtils;
import de.blxckoxymoron.minigames.utils.CommandUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class ArenaCommand implements TabExecutor {

    /*
    Missing Argument for <something>
    Usage: /ask <something>

    /arena <create|delete|teleport|edit> <name> (id)
     */

    public ChatUtils commandChat;

    public ArenaCommand(ChatUtils commandChat) {
        this.commandChat = commandChat;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        switch (args.length) {
            case 0: {
                commandChat.sendError(sender, "No argument for: " + ChatColor.YELLOW + "<create|delete|teleport|edit>");
                return false;
            }
            case 1: {
                commandChat.sendError(sender, "No argument for: " + ChatColor.YELLOW + "<name>");
                return false;
            }
            case 2:
            case 3: {
                //TODO correct arguments

                Minigames plugin = Minigames.getPlugin();
                FileConfiguration config = plugin.getConfig();

                String arenaName = args[1];
                int arenaId = 0;
                if (args.length == 3) {
                    try {
                        arenaId = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        commandChat.sendError(sender,  ChatColor.YELLOW + "[id]: '" + args[2] + "' " + ChatUtils.MessageColor.ERROR + "is not a number!");
                        return false;
                    }
                } else {
                    arenaId = Arena.getNewArenaId();
                }

                switch (args[0]) {
                    case "create": {

                        if (config.contains(Arena.BASE_PATH + "." + arenaId)) {
                            commandChat.sendError(sender,  ChatColor.YELLOW + "id '" + arenaId + "' " + ChatUtils.MessageColor.ERROR + "already exists!");
                            return true;
                        }
                        new Arena(arenaId, arenaName){{
                            setMob(EntityType.ZOGLIN, 3);
                            setToConfig(config);
                        }};
                        plugin.saveConfig();
                        break;
                    }
                    case "delete": {

                        config.set(Arena.BASE_PATH + "." + arenaId, null);
                        Arena.currArenaId = arenaId;
                        plugin.saveConfig();
                        break;
                    }
                    case "teleport": {

                        if (!(sender instanceof Entity)) {commandChat.sendError(sender, "You are not an Entity!"); return false;}
                        Entity senderEntity = (Entity) sender;

                        break;
                    }
                    case "edit": {
                        break;
                    }
                    default: {
                        commandChat.sendError(sender, "Wrong argument for: " + ChatColor.YELLOW + "<create|delete|teleport|edit>");
                    }
                }
                return true;
            }
            default: {
                commandChat.sendError(sender, "Too many Arguments!");
                return false;
            }
        }
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
