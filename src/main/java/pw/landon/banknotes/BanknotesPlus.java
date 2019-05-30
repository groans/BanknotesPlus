package pw.landon.banknotes;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public class BanknotesPlus extends JavaPlugin {

    public static final Logger log = Logger.getLogger("Minecraft");
    public static Economy econ = null;
    public static Permission perms = null;
    public static Chat chat = null;
    private static BanknotesPlus instance;

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getServer().getPluginManager().disablePlugin(this);
        }
        instance = this;
        this.getConfig().options().copyDefaults();
        saveDefaultConfig();
        WithdrawGUI withdrawGui = new WithdrawGUI(this);
        getCommand("withdraw").setExecutor(new WithdrawCommand(this, withdrawGui));
        Bukkit.getPluginManager().registerEvents(new WithdrawGUI(this), this);
        Bukkit.getPluginManager().registerEvents(new RedeemEvent(this), this);
    }

    public static BanknotesPlus getInstance() {
        return instance;
    }

    public boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}