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
        target.sendMessage(ChatColor.GRAY + "[" + ChatColor.AQUA + senderName + ChatColor.GRAY + "]" + ChatColor.GRAY + " | " + ChatColor.WHITE + message);
    }
    public void sendInfo(CommandSender target, String message) {
        target.sendMessage(ChatColor.GRAY + "[" + ChatColor.YELLOW + senderName + ChatColor.GRAY + "]" + ChatColor.GRAY + " | " + ChatColor.DARK_GRAY + message);
    }
    public void sendError(CommandSender target, String message) {
        target.sendMessage(ChatColor.GRAY + "[" + ChatColor.DARK_RED + senderName + ChatColor.GRAY + "]" + ChatColor.DARK_RED + " |Error| " + ChatColor.RED + message);
    }
}
