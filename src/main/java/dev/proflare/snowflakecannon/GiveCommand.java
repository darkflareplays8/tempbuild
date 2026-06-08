package dev.proflare.snowflakecannon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target;

        if (args.length == 0) {
            if (!(sender instanceof Player p)) {
                sender.sendMessage(Component.text("Specify a player.").color(NamedTextColor.RED));
                return true;
            }
            target = p;
        } else {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Component.text("Player not found.").color(NamedTextColor.RED));
                return true;
            }
        }

        target.getInventory().addItem(SnowflakeRod.create());
        sender.sendMessage(Component.text("Given snowflake rod to " + target.getName() + ".").color(NamedTextColor.GREEN));
        return true;
    }
}
