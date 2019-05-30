package pw.landon.banknotes;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;

public class WithdrawGUI implements Listener {

    private BanknotesPlus main;
    public WithdrawGUI(BanknotesPlus main) {
        this.main = main;
    }

    public ItemStack createGuiItem(String name, ArrayList<String> desc, Material mat) {
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(desc);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack firstItem() {
        List<String> firstLore = main.getConfig().getStringList("gui.first.lore");
        for(int i = 0; i < firstLore.size(); i++) {
            firstLore.set(i, ChatColor.translateAlternateColorCodes('&', firstLore.get(i)));
        }
        ItemStack item = new ItemStack(Material.valueOf(main.getConfig().getString("gui.first.item")), main.getConfig().getInt("gui.first.amount"));
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("gui.first.name")));
        itemMeta.setLore(firstLore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack secondItem() {
        List<String> secondLore = main.getConfig().getStringList("gui.second.lore");
        for(int i = 0; i < secondLore.size(); i++) {
            secondLore.set(i, ChatColor.translateAlternateColorCodes('&', secondLore.get(i)));
        }
        ItemStack item = new ItemStack(Material.valueOf(main.getConfig().getString("gui.second.item")), main.getConfig().getInt("gui.second.amount"));
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("gui.second.name")));
        itemMeta.setLore(secondLore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack thirdItem() {
        List<String> thirdLore = main.getConfig().getStringList("gui.third.lore");
        for(int i = 0; i < thirdLore.size(); i++) {
            thirdLore.set(i, ChatColor.translateAlternateColorCodes('&', thirdLore.get(i)));
        }
        ItemStack item = new ItemStack(Material.valueOf(main.getConfig().getString("gui.third.item")), main.getConfig().getInt("gui.third.amount"));
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("gui.third.name")));
        itemMeta.setLore(thirdLore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack customItem() {
        List<String> customLore = main.getConfig().getStringList("gui.custom.lore");
        for(int i = 0; i < customLore.size(); i++) {
            customLore.set(i, ChatColor.translateAlternateColorCodes('&', customLore.get(i)));
        }
        ItemStack item = new ItemStack(Material.valueOf(main.getConfig().getString("gui.custom.item")), main.getConfig().getInt("gui.custom.amount"));
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("gui.custom.name")));
        itemMeta.setLore(customLore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack banknote(int value) {
        List<String> customLore = main.getConfig().getStringList("banknote.lore");
        String formattedValue = String.format("%,d", value);
        customLore.add("&7");
        customLore.add(main.getConfig().getString("banknote.value").replace("%value%", formattedValue));
        for(int i = 0; i < customLore.size(); i++) {
            customLore.set(i, ChatColor.translateAlternateColorCodes('&', customLore.get(i)));
        }
        ItemStack item = new ItemStack(Material.valueOf(main.getConfig().getString("banknote.item")), 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("banknote.name")));
        itemMeta.setLore(customLore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public void openGUI(Player player) {
        final Inventory withdrawShop = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("options.title")));
        withdrawShop.setItem(10, firstItem());
        withdrawShop.setItem(12, secondItem());
        withdrawShop.setItem(14, thirdItem());
        withdrawShop.setItem(16, customItem());
        player.openInventory(withdrawShop);
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        Player playerClicker = (Player) e.getWhoClicked();
        if (ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("options.title")).equals(e.getClickedInventory().getTitle())) {
            if (e.getSlot() == 10) {
                e.setCancelled(true);
                String formattedValue = String.format("%,d", main.getConfig().getInt("gui.first.value"));
                main.econ.withdrawPlayer(playerClicker, main.getConfig().getInt("gui.first.value"));
                playerClicker.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.purchased")).replace("%value%", formattedValue));
                playerClicker.getInventory().addItem(banknote(main.getConfig().getInt("gui.first.value")));
            } if (e.getSlot() == 12) {
                e.setCancelled(true);
                String formattedValue = String.format("%,d", main.getConfig().getInt("gui.second.value"));
                main.econ.withdrawPlayer(playerClicker, main.getConfig().getInt("gui.second.value"));
                playerClicker.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.purchased")).replace("%value%", formattedValue));
                playerClicker.getInventory().addItem(banknote(main.getConfig().getInt("gui.second.value")));
            } if (e.getSlot() == 14) {
                e.setCancelled(true);
                String formattedValue = String.format("%,d", main.getConfig().getInt("gui.third.value"));
                main.econ.withdrawPlayer(playerClicker, main.getConfig().getInt("gui.third.value"));
                playerClicker.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.purchased")).replace("%value%", formattedValue));
                playerClicker.getInventory().addItem(banknote(main.getConfig().getInt("gui.third.value")));
            } if (e.getSlot() == 16) {
                e.setCancelled(true);
                new AnvilGUI(main, playerClicker, "What is the meaning of life?", (player, reply) -> {
                    if (reply.equalsIgnoreCase("you")) {
                        player.sendMessage("You have magical powers!");
                        return null;
                    }
                    return "Incorrect.";
                });
                String formattedValue = String.format("%,d", main.getConfig().getInt("gui.first.value"));
                main.econ.withdrawPlayer(playerClicker, main.getConfig().getInt("gui.first.value"));
                playerClicker.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.purchased")).replace("%value%", formattedValue));
                playerClicker.getInventory().addItem(banknote(main.getConfig().getInt("gui.first.value")));
            }
        }
    }
}
