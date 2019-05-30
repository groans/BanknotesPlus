package pw.landon.banknotes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class WithdrawCommand implements CommandExecutor, Listener {

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
            gui.openGUI(player);
        }
        return false;
    }
}
