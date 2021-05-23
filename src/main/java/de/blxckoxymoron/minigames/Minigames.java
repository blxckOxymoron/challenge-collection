package de.blxckoxymoron.minigames;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Minigames extends JavaPlugin {

    public Minigames plugin;
    
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        Bukkit.getLogger().fine("Minigames enabled");

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
}
