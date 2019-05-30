package pw.landon.banknotes;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.text.DecimalFormat;

public class BanknotesCommand implements CommandExecutor {

    private BanknotesPlus main;
    public BanknotesCommand(BanknotesPlus main) {this.main = main;}

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("banknotes.admin")) {
            int argumentCount = args.length;
            switch (argumentCount) {
                case 0:
                    player.sendMessage("");
                    player.sendMessage(ChatColor.GREEN + "/banknotes reload");
                    player.sendMessage(ChatColor.GREEN + "Plugin by @Religion on MCM.");
                    player.sendMessage("");
                    break;
                case 1:
                    String arg1 = args[0].toLowerCase();
                    if (arg1.equals("reload")) {
                        long l1 = System.currentTimeMillis();
                        main.saveConfig();
                        main.reloadConfig();
                        long l2 = System.currentTimeMillis() - l1;
                        DecimalFormat formatter = new DecimalFormat("###.#");
                        player.sendMessage("");
                        player.sendMessage(ChatColor.GREEN + "Configuration reloaded successfully in " + ChatColor.DARK_GREEN + formatter.format(l2) + ChatColor.GREEN + "ms.");
                        player.sendMessage("");
                    }
                    break;
                default:
                    player.sendMessage("");
                    player.sendMessage(ChatColor.GREEN + "/banknotes reload");
                    player.sendMessage(ChatColor.GREEN + "Plugin by @Religion on MCM.");
                    player.sendMessage("");
                    break;
            }
        }
        return false;
    }
}
