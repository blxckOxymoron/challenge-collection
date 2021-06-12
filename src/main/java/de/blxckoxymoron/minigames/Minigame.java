package de.blxckoxymoron.minigames;

import de.blxckoxymoron.minigames.utils.ChatUtils;

public class Minigame {

    public ChatUtils chat;
    public String gameName;

    public Minigame() {
        setGameName("Default Minigame");
    }

    public void registerCommands(Minigames plugin) {

    }
    public void registerEvents(Minigames plugin) {

    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
        this.chat = new ChatUtils(gameName);
    }

    public void onDisable() {}

    public ChatUtils getChat() {
        return chat;
    }

    public String getGameName() {
        return gameName;
    }
}
