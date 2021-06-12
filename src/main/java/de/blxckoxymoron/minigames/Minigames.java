package de.blxckoxymoron.minigames;

import de.blxckoxymoron.minigames.pvearena.PvEArena;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public final class Minigames extends JavaPlugin {

    public static Minigames plugin;
    public static Set<Minigame> minigames = new HashSet<>();
    
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        plugin.saveDefaultConfig();
        Bukkit.getLogger().fine("Minigames enabled");

        registerMinigame(new PvEArena());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Minigame minigame : minigames) {
            minigame.onDisable();
        }
    }

    public void commandRegistration() {
        
    }

    public static Minigames getPlugin() {
        return plugin;
    }

    public void registerMinigame(Minigame game) {
        game.registerCommands(this);
        game.registerEvents(this);
        minigames.add(game);
    }
}
