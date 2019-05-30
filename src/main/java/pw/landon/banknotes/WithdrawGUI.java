package pw.landon.banknotes;

import net.wesjd.anvilgui.AnvilGUI;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;

public class WithdrawGUI implements Listener {

    private BanknotesPlus main;
    public WithdrawGUI(BanknotesPlus main) {this.main = main;}


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

    public ItemStack banknote(int value, Player player) {
        List<String> customLore = main.getConfig().getStringList("banknote.lore");
        String formattedValue = String.format("%,d", value);
        customLore.add("&7");
        customLore.add(main.getConfig().getString("banknote.value").replace("%value%", formattedValue));
        for(int i = 0; i < customLore.size(); i++) {
            customLore.set(i, ChatColor.translateAlternateColorCodes('&', customLore.get(i)));
            String playerLore = customLore.get(i).replace("%player%", player.getDisplayName());
            customLore.set(i, playerLore);
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
            e.setCancelled(true);
            if (e.getSlot() == 10) {
                int value = main.getConfig().getInt("gui.first.value");
                if (main.econ.getBalance(playerClicker) >= value) {
                    playerClicker.playSound(playerClicker.getLocation(), Sound.valueOf(main.getConfig().getString("options.buysound")), 3.0F, 0.5F);
                    String formattedValue = String.format("%,d", value);
                    main.econ.withdrawPlayer(playerClicker, value);
                    playerClicker.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.purchased")).replace("%value%", formattedValue));
                    playerClicker.getInventory().addItem(banknote(value, playerClicker));
                } else {
                    double difference = value - main.econ.getBalance(playerClicker);
                    playerClicker.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.nomoney").replace("%difference%", Double.toString(difference))));
                }
            } if (e.getSlot() == 12) {
                int value = main.getConfig().getInt("gui.second.value");
                if (main.econ.getBalance(playerClicker) >= value) {
                    playerClicker.playSound(playerClicker.getLocation(), Sound.valueOf(main.getConfig().getString("options.buysound")), 3.0F, 0.5F);
                    String formattedValue = String.format("%,d", value);
                    main.econ.withdrawPlayer(playerClicker, value);
                    playerClicker.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.purchased")).replace("%value%", formattedValue));
                    playerClicker.getInventory().addItem(banknote(value, playerClicker));
                } else {
                    double difference = value - main.econ.getBalance(playerClicker);
                    playerClicker.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.nomoney").replace("%difference%", Double.toString(difference))));
                }
            } if (e.getSlot() == 14) {
                int value = main.getConfig().getInt("gui.third.value");
                if (main.econ.getBalance(playerClicker) >= value) {
                    playerClicker.playSound(playerClicker.getLocation(), Sound.valueOf(main.getConfig().getString("options.buysound")), 3.0F, 0.5F);
                    String formattedValue = String.format("%,d", value);
                    main.econ.withdrawPlayer(playerClicker, value);
                    playerClicker.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.purchased")).replace("%value%", formattedValue));
                    playerClicker.getInventory().addItem(banknote(value, playerClicker));
                } else {
                    double difference = value - main.econ.getBalance(playerClicker);
                    playerClicker.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.nomoney").replace("%difference%", Double.toString(difference))));
                }
            } if (e.getSlot() == 16) {
                new AnvilGUI(main, playerClicker, "Enter amount...", (player, reply) -> {
                    if (StringUtils.isNumeric(reply)) {
                        int value = Integer.parseInt(reply);
                        if (main.econ.getBalance(player) >= value) {
                            player.playSound(player.getLocation(), Sound.valueOf(main.getConfig().getString("options.buysound")), 3.0F, 0.5F);
                            String formattedValue = String.format("%,d", value);
                            main.econ.withdrawPlayer(player, value);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.purchased")).replace("%value%", formattedValue));
                            player.getInventory().addItem(banknote(value, player));
                            return null;
                        } else {
                            double difference = value - main.econ.getBalance(player);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.nomoney").replace("%difference%", Double.toString(difference))));
                            return "§cNot enough money.";
                        }
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.error")));
                    return "§cEnter a valid number.";
                });
            }
        }
    }
}
