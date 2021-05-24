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
        target.sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + senderName + ChatColor.WHITE + "]" + ChatColor.GRAY + " <> " + MessageColor.MESSAGE + message);
    }
    public void sendInfo(CommandSender target, String message) {
        target.sendMessage(ChatColor.WHITE + "[" + ChatColor.YELLOW + senderName + ChatColor.GRAY + "]" + ChatColor.GRAY + " <> " + MessageColor.INFO + message);
    }
    public void sendError(CommandSender target, String message) {
        target.sendMessage(ChatColor.WHITE + "[" + ChatColor.DARK_RED + senderName + ChatColor.WHITE + "]" + ChatColor.DARK_RED + " <Error> " + MessageColor.ERROR + message);
    }

    public enum MessageColor {

        MESSAGE(ChatColor.WHITE),
        INFO(ChatColor.DARK_GRAY),
        ERROR(ChatColor.RED);

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
