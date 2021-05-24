package de.blxckoxymoron.minigames;

import de.blxckoxymoron.minigames.pvearena.PvEArena;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Minigames extends JavaPlugin {

    public Minigames plugin;
    
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
    }

    public void commandRegistration() {
        
    }

    public Minigames getPlugin() {
        return plugin;
    }

    public void registerMinigame(Minigame game) {
        game.registerCommands(this);
        game.registerEvents(this);
    }
}
