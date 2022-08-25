package meteordevelopment.meteoressentials.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import meteordevelopment.meteoressentials.Config;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinLeaveListener implements Listener {
    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        event.joinMessage(Component.text(PlaceholderAPI.setPlaceholders(event.getPlayer(), Config.JOIN_MESSAGE)));
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        event.quitMessage(Component.text(PlaceholderAPI.setPlaceholders(event.getPlayer(), Config.LEAVE_MESSAGE)));
    }
}
