package de.blxckoxymoron.minigames.pvearena;

import de.blxckoxymoron.minigames.Minigame;
import de.blxckoxymoron.minigames.Minigames;
import de.blxckoxymoron.minigames.pvearena.commands.ArenaCommand;
import de.blxckoxymoron.minigames.utils.CommandUtils;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

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
    public void registerCommands(Minigames plugin) {
        CommandUtils.setTabExecutor(plugin.getCommand("arena"), new ArenaCommand(this.chat));
    }

    @Override
    public void registerEvents(Minigames plugin) {

    }

}
