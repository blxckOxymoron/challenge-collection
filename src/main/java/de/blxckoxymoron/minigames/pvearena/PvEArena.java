package de.blxckoxymoron.minigames.pvearena;

import de.blxckoxymoron.minigames.Minigame;
import de.blxckoxymoron.minigames.Minigames;
import de.blxckoxymoron.minigames.pvearena.commands.ArenaCommand;
import de.blxckoxymoron.minigames.utils.CommandUtils;

public class PvEArena extends Minigame {

    @Override
    public void registerCommands(Minigames plugin) {
        CommandUtils.setTabExecutor(plugin.getCommand("arena"), new ArenaCommand());
    }

    @Override
    public void registerEvents(Minigames plugin) {

    }
}
