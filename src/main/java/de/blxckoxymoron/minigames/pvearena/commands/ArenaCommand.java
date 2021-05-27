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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

        Minigames plugin = Minigames.getPlugin();
        FileConfiguration config = plugin.getConfig();

        switch (args.length) {
            case 0: {
                commandChat.sendError(sender, "No argument for: " + ChatColor.YELLOW + "<create|delete|teleport|edit|list>");
                return false;
            }
            case 1: {

                if ("list".equals(args[0])) {
                    for (String id : config.getConfigurationSection(Arena.BASE_PATH).getKeys(false)) {
                        commandChat.sendMessage(sender, "" +
                                "Arena with name: " + ChatColor.YELLOW + config.getString(Arena.BASE_PATH + "." + id + ".name") + ChatUtils.MessageColor.MESSAGE +
                                " and id: " + ChatColor.YELLOW + id);
                    }
                    return true;
                }
                commandChat.sendError(sender, "No argument for: " + ChatColor.YELLOW + "<name>");
                return false;

            }
            case 2:
            case 3: {
                //TODO correct arguments

                String arenaName = args[1];
                int arenaId = -1;
                if (args.length == 3) {
                    try {
                        arenaId = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        commandChat.sendError(sender,  ChatColor.YELLOW + "[id]: '" + args[2] + "' " + ChatUtils.MessageColor.ERROR + "is not a number!");
                        return false;
                    }
                    if (arenaId < 0) {
                        commandChat.sendError(sender,  ChatColor.YELLOW + "[id]: '" + args[2] + "' " + ChatUtils.MessageColor.ERROR + "is not greater than 0!");
                        return false;
                    }
                }

                switch (args[0]) {
                    case "create": {

                        if (arenaId == -1) {
                            arenaId = Arena.getNewArenaId();
                        }

                        if (config.contains(Arena.BASE_PATH + "." + arenaId)) {
                            commandChat.sendError(sender,  ChatColor.YELLOW + "id '" + arenaId + "' " + ChatUtils.MessageColor.ERROR + "already exists!");
                            return true;
                        }
                        new Arena(arenaId, arenaName){{
                            setToConfig(config);
                        }};
                        plugin.saveConfig();

                        commandChat.sendMessage(sender, "Created Arena with name " + ChatColor.YELLOW + arenaName + ChatUtils.MessageColor.MESSAGE + " and id " + ChatColor.YELLOW + arenaId);
                        break;
                    }
                    case "delete": {

                        Set<String> arenaIds = Arena.getArenaIds(arenaName);
                        if (arenaId == -1) {
                            if (arenaIds.size() > 1) {
                                commandChat.sendError(sender, "" +
                                        "More than 1 arenas with name " + ChatColor.YELLOW + arenaName + ChatUtils.MessageColor.ERROR +
                                        "! Please specify one of these ids: " + ChatColor.YELLOW + arenaIds.toString());
                                return true;
                            } else if (arenaIds.size() < 1) {
                                commandChat.sendError(sender, "No arena with name " + ChatColor.YELLOW + arenaName);
                                return true;
                            } else {
                                arenaId = Integer.parseInt((String) arenaIds.toArray()[0]);
                            }
                        } else if (!arenaIds.contains(Integer.toString(arenaId))) {
                            commandChat.sendError(sender, "No arena with name " + ChatColor.YELLOW + arenaName + ChatUtils.MessageColor.ERROR + " and id " + ChatColor.YELLOW + arenaId);
                            return true;

                        }

                        config.set(Arena.BASE_PATH + "." + arenaId, null);
                        Arena.currArenaId = arenaId;
                        plugin.saveConfig();

                        commandChat.sendMessage(sender, "Deleted Arena with name: " + ChatColor.YELLOW + arenaName + ChatUtils.MessageColor.MESSAGE + " and id: " + ChatColor.YELLOW + arenaId);
                        break;
                    }
                    case "list": {
                        commandChat.sendError(sender, "Too many arguments");
                        return false;
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
                        commandChat.sendError(sender, "Wrong argument for: " + ChatColor.YELLOW + "<create|delete|teleport|edit|list>");
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

        List<String> empty = new ArrayList<>();

        switch (args.length) {
            case 1: {
                return CommandUtils.matchingOptions(args[0], "create", "delete", "teleport", "edit", "list");
            }
            case 2: {
                if (!Arrays.asList("create", "delete", "teleport", "edit").contains(args[0])) {
                    return empty;
                }
                return CommandUtils.matchingOptions(args[1], Arena.getArenaNames().toArray(new String[0]));
            }
        }

        return empty;

    }
}
