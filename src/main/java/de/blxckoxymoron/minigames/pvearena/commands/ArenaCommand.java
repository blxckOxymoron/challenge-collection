package de.blxckoxymoron.minigames.pvearena.commands;

import de.blxckoxymoron.minigames.Minigame;
import de.blxckoxymoron.minigames.Minigames;
import de.blxckoxymoron.minigames.pvearena.Arena;
import de.blxckoxymoron.minigames.utils.ChatUtils;
import de.blxckoxymoron.minigames.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

        switch (args.length) {
            case 0: {
                commandChat.sendError(sender, "No argument for: " + ChatColor.YELLOW + "<create|delete|teleport|edit>");
                return false;
            }
            case 1: {
                commandChat.sendError(sender, "No argument for: " + ChatColor.YELLOW + "<name>");
                return false;
            }
            case 2: {
                //TODO correct arguments

                Minigames plugin = Minigames.getPlugin();
                FileConfiguration config = plugin.getConfig();

                switch (args[0]) {
                    case "create": {
//                        config.set("arenas." + args[1] + ".mobs", new HashMap<String, Integer>(){{put(EntityType.ZOMBIE.name(), 90);}});
//                        senderEntity.getWorld().spawnEntity(senderEntity.getLocation(),EntityType.valueOf("zombie".toUpperCase()));
                        new Arena(1, args[1]){{
                            setMob(EntityType.ZOGLIN, 3);
                            setToConfig(config);
                        }};
                        plugin.saveConfig();
                        System.out.println(
                            new Arena(1, args[1] + "lel") {{
                                setFromConfig(config);
                            }}.getName().equals(args[1]) ? "YeSsSsS" : "no."
                        );
                        break;
                    }
                    case "delete": {
                        config.set("arenas." + args[1], null);
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
