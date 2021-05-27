package de.blxckoxymoron.minigames.pvearena;

import de.blxckoxymoron.minigames.Minigame;
import de.blxckoxymoron.minigames.Minigames;
import de.blxckoxymoron.minigames.pvearena.commands.ArenaCommand;
import de.blxckoxymoron.minigames.utils.CommandUtils;
import org.bukkit.configuration.file.FileConfiguration;

public class PvEArena extends Minigame {

    public PvEArena() {
        setGameName("PvE Arena");
    }

    @Override
    public void registerCommands(Minigames plugin) {
        CommandUtils.setTabExecutor(plugin.getCommand("arena"), new ArenaCommand(this.chat));
    }

    @Override
    public void registerEvents(Minigames plugin) {

    }

}
