package de.blxckoxymoron.minigames.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChatUtils {

    public String senderName;

    public ChatUtils() {
        this.senderName = "Server";
    }
    public ChatUtils(String senderName) {
        this.senderName = senderName;
    }

    public void sendMessage(CommandSender target, String message) {
        target.sendMessage(ChatColor.WHITE + "[" + MessageColor.NAME + senderName + ChatColor.WHITE + "] " + MessageColor.MESSAGE + message);
    }
    public void sendInfo(CommandSender target, String message) {
        target.sendMessage(ChatColor.WHITE + "[" + MessageColor.NAME + senderName + ChatColor.WHITE + "] " + MessageColor.INFO + message);
    }
    public void sendError(CommandSender target, String message) {
        target.sendMessage(ChatColor.WHITE + "[" + MessageColor.NAME + senderName + ChatColor.WHITE + "]" + ChatColor.RED + " Error: " + MessageColor.ERROR + message);
    }

    public enum MessageColor {

        MESSAGE(ChatColor.WHITE),
        INFO(ChatColor.DARK_GRAY),
        ERROR(ChatColor.RED),
        NAME(ChatColor.GREEN);

        private final ChatColor color;

        MessageColor(ChatColor color) {
            this.color = color;
        }

        @Override
        public String toString() {
            return this.color.toString();
        }
    }
}
