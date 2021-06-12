package de.blxckoxymoron.minigames.pvearena;

import de.blxckoxymoron.minigames.Minigame;
import de.blxckoxymoron.minigames.Minigames;
import de.blxckoxymoron.minigames.pvearena.commands.ArenaCommand;
import de.blxckoxymoron.minigames.pvearena.listeners.InventoryCloseListener;
import de.blxckoxymoron.minigames.utils.CommandUtils;
import org.bukkit.*;
import org.bukkit.plugin.PluginManager;

import java.util.LinkedList;
import java.util.List;

public class PvEArena extends Minigame {

    public static String worldName = "PvEArena-World";
    public static List<Arena> loadedArenas = new LinkedList<>();

    public PvEArena() {
        setGameName("PvE Arena");
        WorldCreator creator = new WorldCreator(worldName);
        creator.type(WorldType.FLAT);
        creator.generateStructures(false);
        World world = creator.createWorld();
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.DO_MOB_LOOT, false);
    }

    @Override
    public void onDisable() {
        Arena.updateInventories();
        for (Arena a : loadedArenas) {
            a.setToConfig();
        }
        Minigames.getPlugin().saveConfig();
    }

    @Override
    public void registerCommands(Minigames plugin) {
        CommandUtils.setTabExecutor(plugin.getCommand("arena"), new ArenaCommand(this.chat));
    }

    @Override
    public void registerEvents(Minigames plugin) {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new InventoryCloseListener(), plugin);
    }

}
