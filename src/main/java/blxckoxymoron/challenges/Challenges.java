package blxckoxymoron.challenges;

import blxckoxymoron.challenges.commands.ChallengeCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Challenges extends JavaPlugin {

    public static Challenges plugin;

    @Override
    public void onEnable() {
        plugin = this;
        // Plugin startup logic
        Bukkit.getLogger().fine("gestartet");
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerCommands() {
        getCommand("challenge").setExecutor(new ChallengeCommand());
    }
}
