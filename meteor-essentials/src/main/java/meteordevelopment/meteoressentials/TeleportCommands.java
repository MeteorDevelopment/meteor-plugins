package meteordevelopment.meteoressentials;

import meteordevelopment.meteoressentials.commands.Commands;
import meteordevelopment.meteoressentials.commands.MeteorCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportCommands {
    public static void register() {
        for (String name : Config.TELEPORT_COMMANDS.keySet()) {
            String cmd = Config.TELEPORT_COMMANDS.get(name);

            String location = cmd;
            Perm perm = null;

            // Parse permission
            if (cmd.contains(";")) {
                int i = cmd.indexOf(";");

                location = cmd.substring(0, i - 1);
                perm = Perms.get(MeteorEssentials.INSTANCE, "teleport." + cmd.substring(i).trim());
            }

            // Register command
            Commands.register(new TeleportCommand(name, location, perm));
        }
    }

    private static class TeleportCommand extends MeteorCommand {
        private final String location;

        public TeleportCommand(String name, String location, Perm perm) {
            super(MeteorEssentials.INSTANCE, name, "Teleports to " + name + ".", "/" + name, perm);
            this.location = location;
        }

        @Override
        protected boolean onCommand(CommandSender sender, String label, String[] args) {
            if (!(sender instanceof Player player)) return false;

            String[] split = location.split(",");

            // Parse world
            World world = Bukkit.getWorld(split[0].trim());
            if (world == null) {
                sender.sendMessage(Config.PREFIX + ChatColor.RED + "Invalid world.");
                return false;
            }

            // Parse position
            double x, y, z;
            String position = split[1].trim();

            if (position.equals("%spawn%")) {
                Location loc = world.getSpawnLocation();

                x = loc.getBlockX() + 0.5;
                y = loc.getBlockY() + 0.5;
                z = loc.getBlockZ() + 0.5;
            }
            else {
                String[] positionSplit = position.split(" ");
                try {
                    x = Integer.parseInt(positionSplit[0]) + 0.5;
                    y = Integer.parseInt(positionSplit[1]) + 0.5;
                    z = Integer.parseInt(positionSplit[2]) + 0.5;
                }
                catch (IndexOutOfBoundsException | NumberFormatException ignored) {
                    sender.sendMessage(Config.PREFIX + ChatColor.RED + "Invalid position.");
                    return false;
                }
            }

            // Teleport
            player.teleport(new Location(world, x, y, z), PlayerTeleportEvent.TeleportCause.COMMAND);
            return true;
        }
    }
}
