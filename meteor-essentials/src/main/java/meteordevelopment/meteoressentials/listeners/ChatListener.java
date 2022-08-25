package meteordevelopment.meteoressentials.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import meteordevelopment.meteoressentials.Config;
import meteordevelopment.meteoressentials.MeteorEssentials;
import meteordevelopment.meteoressentials.Perm;
import meteordevelopment.meteoressentials.Perms;
import meteordevelopment.meteoressentials.chat.Ignores;
import meteordevelopment.meteoressentials.chat.Mutes;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatListener implements Listener {
    private final Perm bypassDisabledCommandsPerm = Perms.get(MeteorEssentials.INSTANCE, "bypass_disabled_commands");

    private final Map<UUID, String> lastMsgs = new HashMap<>();

    @EventHandler
    private void onAsyncChat(AsyncChatEvent event) {
        Player sender = event.getPlayer();

        // Mutes
        if (Mutes.isMuted(sender)) {
            sender.sendMessage(String.format("%s%sYou cannot talk whilst muted.", Config.PREFIX, ChatColor.RED));
            event.setCancelled(true);
            return;
        }

        String msg = event.message().toString().toLowerCase();

        // Anti spam
        String lastMsg = lastMsgs.get(sender.getUniqueId());
        if (lastMsg != null && lastMsg.equals(msg)) {
            event.setCancelled(true);
            return;
        }

        // Ignores
        event.viewers().removeIf(audience -> (audience instanceof Player player) && Ignores.hasReceiverIgnored(sender, player));

        // ...
        if (!msg.startsWith("/")) lastMsgs.put(sender.getUniqueId(), msg);
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        lastMsgs.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    private void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String msg = event.getMessage().toLowerCase();

        // Disabled commands
        if (!bypassDisabledCommandsPerm.has(event.getPlayer())) {
            for (String disabledCommand : Config.DISABLED_COMMANDS) {
                if (msg.startsWith(disabledCommand)) {
                    event.getPlayer().sendMessage(String.format("%s%sYou cannot use this command.", Config.PREFIX, ChatColor.RED));
                    event.setCancelled(true);
                    return;
                }
            }
        }

        // Mutes
        if (isPrivateMessage(msg) && Mutes.isMuted(event.getPlayer())) {
            event.getPlayer().sendMessage(String.format("%s%sYou cannot talk whilst muted.", Config.PREFIX, ChatColor.RED));
            event.setCancelled(true);
            return;
        }

        // Redirects
        if (msg.startsWith("/w ")) {
            event.setMessage("/msg " + event.getMessage().substring(3));
        }
        else if (msg.startsWith("/tell ")) {
            event.setMessage("/msg " + event.getMessage().substring(6));
        }
    }

    private boolean isPrivateMessage(String command) {
        return command.startsWith("/w ") || command.startsWith("/msg ") || command.startsWith("/r ") || command.startsWith("/tell ");
    }
}
