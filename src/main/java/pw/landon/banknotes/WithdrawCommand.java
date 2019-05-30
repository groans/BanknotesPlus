package pw.landon.banknotes;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class WithdrawCommand implements CommandExecutor {

    private BanknotesPlus main;
    private WithdrawGUI gui;

    public WithdrawCommand(BanknotesPlus main, WithdrawGUI gui) {
        this.main = main;;
        this.gui = gui;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("The command can not be ran with Console.");
        } else if (sender instanceof Player) {
            Player player = (Player) sender;
            int argumentCount = args.length;
            switch (argumentCount) {
                case 0:
                    gui.openGUI(player);
                    break;
                case 1:
                    String arg1 = args[0].toLowerCase();
                    if (StringUtils.isNumeric(arg1)) {
                        int value = Integer.parseInt(arg1);
                        if (main.econ.getBalance(player) >= value) {
                            player.playSound(player.getLocation(), Sound.valueOf(main.getConfig().getString("options.buysound")), 3.0F, 0.5F);
                            String formattedValue = String.format("%,d", value);
                            main.econ.withdrawPlayer(player, value);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.purchased")).replace("%value%", formattedValue));
                            player.getInventory().addItem(gui.banknote(value, player));
                        } else {
                            double difference = value - main.econ.getBalance(player);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.nomoney").replace("%difference%", Double.toString(difference))));
                        }
                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.error")));
                    }
                    break;
                default:
                    gui.openGUI(player);
                    break;
            }
        }
        return false;
    }
}
