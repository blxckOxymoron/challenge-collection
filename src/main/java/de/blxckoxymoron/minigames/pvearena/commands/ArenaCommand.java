package de.blxckoxymoron.minigames.pvearena.commands;

import de.blxckoxymoron.minigames.Minigames;
import de.blxckoxymoron.minigames.pvearena.Arena;
import de.blxckoxymoron.minigames.utils.ChatUtils;
import de.blxckoxymoron.minigames.utils.CommandUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
    public static List<String> arenaArguments = Arrays.asList("delete", "teleport", "edit", "mobloot", "bossloot", "bossmob");
    public static List<String> allOptions = Arrays.asList("delete", "teleport", "edit", "create", "list", "mobloot", "bossloot", "bossmob", "next");

    public ArenaCommand(ChatUtils commandChat) {
        this.commandChat = commandChat;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Minigames plugin = Minigames.getPlugin();
        FileConfiguration config = plugin.getConfig();


        switch (args.length) {
            case 0: {
                // TODO: 09.06.2021 Change info 
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
            case 3:
            case 4: {
                //TODO correct arguments

                String arenaName = args[1];
                int arenaId = -1;
                if (args.length >= 3) {
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
                if (args.length >= 4 && !args[0].equals("bossmob")) {
                    commandChat.sendError(sender, "Too many Arguments!");
                    return false;
                }

                if (arenaArguments.contains(args[0])) {
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
                }

//                sender.sendMessage(String.valueOf(arenaId));


                switch (args[0]) {
                    case "create": {

                        if (arenaId == -1) {
                            arenaId = Arena.getNewArenaId();
                        }

                        if (config.contains(Arena.BASE_PATH + "." + arenaId)) {
                            commandChat.sendError(sender,  ChatColor.YELLOW + "id '" + arenaId + "' " + ChatUtils.MessageColor.ERROR + "already exists!");
                            return true;
                        }
                        Arena.createNewAndSave(arenaId, arenaName);
                        plugin.saveConfig();

                        commandChat.sendMessage(sender, "Created Arena with name " + ChatColor.YELLOW + arenaName + ChatUtils.MessageColor.MESSAGE + " and id " + ChatColor.YELLOW + arenaId);
                        break;
                    }
                    case "delete": {

                        Arena.deleteArena(arenaId);

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

                        senderEntity.teleport(Arena.getArena(arenaId).getSpawnLocation());

                        break;
                    }
                    case "bosssmob": {
                        if (args.length != 4) {
                            commandChat.sendError(sender, "To few Arguments!");
                            return false;
                        } else {
                            try {
                                Arena arena = Arena.getArena(arenaId);
                                arena.setBoss(EntityType.valueOf(args[3]));
                            } catch (IllegalArgumentException e) {
                                commandChat.sendError(sender, "Unknown Argument: " + args[3]);
                                return false;
                            }
                        }
                        break;
                    }
                    case "mobloot": {
                        if (!(sender instanceof Player)) {commandChat.sendError(sender, "You are not a Player!"); return false;}
                        Player senderPlayer = (Player) sender;

                        senderPlayer.openInventory(Arena.getArena(arenaId).getLootInventory(Arena.LootInventory.DEFAULT_LOOT));
                        break;
                    }
                    case "bossloot": {
                        if (!(sender instanceof Player)) {commandChat.sendError(sender, "You are not a Player!"); return false;}
                        Player senderPlayer = (Player) sender;

                        senderPlayer.openInventory(Arena.getArena(arenaId).getLootInventory(Arena.LootInventory.BOSS_LOOT));
                        break;
                    }
                    case "edit": {
                        if (!(sender instanceof Player)) {commandChat.sendError(sender, "You are not an Entity!"); return false;}
                        Player senderEntity = (Player) sender;

                        ItemStack stack = new ItemStack(Material.BAT_SPAWN_EGG);
                        if (senderEntity.getInventory().getItemInMainHand().getType().name().endsWith("_SPAWN_EGG")) {
                            stack = senderEntity.getInventory().getItemInMainHand();
                        }

                        String entityName = stack.getType().name().replace("_SPAWN_EGG", "");
                        sender.sendMessage(entityName);
                        senderEntity.getWorld().spawnEntity(senderEntity.getLocation(), EntityType.valueOf(entityName));
                        break;
                    }
                    case "next": {

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
                return CommandUtils.matchingOptions(args[0], allOptions.toArray(new String[0]));
            }
            case 2: {
                if (!arenaArguments.contains(args[0])) {
                    return empty;
                }
                return CommandUtils.matchingOptions(args[1], Arena.getArenaNames().toArray(new String[0]));
            }
            case 3: {
                if (!arenaArguments.contains(args[0])) {
                    return empty;
                }
                return CommandUtils.matchingOptions(args[2], Arena.getArenaIds(args[1]).toArray(new String[0]));
            }
            case 4: {
                if (args[0].equals("bossmob")) {
                    List<String> entityTypes = new ArrayList<>();
                    for (EntityType entityType : EntityType.values()) {
                        entityTypes.add(entityType.name());
                    }
                    return entityTypes;
                }
            }
        }

        return empty;

    }
}
