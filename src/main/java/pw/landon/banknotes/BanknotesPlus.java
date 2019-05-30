package pw.landon.banknotes;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class BanknotesPlus extends JavaPlugin {

    public static Economy econ = null;
    private static BanknotesPlus instance;

    @Override
    public void onEnable() {
        instance = this;
        if (!setupEconomy()) {
            getServer().getPluginManager().disablePlugin(this);
            System.out.println("Vault is not installed. Plugin disabling.");
        }
        this.getConfig().options().copyDefaults();
        saveDefaultConfig();
        WithdrawGUI withdrawGui = new WithdrawGUI(this);
        getCommand("withdraw").setExecutor(new WithdrawCommand(this, withdrawGui));
        getCommand("banknotes").setExecutor(new BanknotesCommand(this));
        Bukkit.getPluginManager().registerEvents(new WithdrawGUI(this), this);
        Bukkit.getPluginManager().registerEvents(new RedeemEvent(this), this);
    }

    public static BanknotesPlus getInstance() {return instance;}

    // Setup Vault
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