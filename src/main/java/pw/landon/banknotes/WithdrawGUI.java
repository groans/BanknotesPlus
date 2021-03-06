package pw.landon.banknotes;

import net.wesjd.anvilgui.AnvilGUI;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
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

    public ItemStack banknote(Long value, Player player) {
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
            if (e.getCurrentItem() == null || e.getCurrentItem().equals(new ItemStack(Material.AIR))){
                return;
            }
            if (e.getSlot() == 10) {
                Long value = main.getConfig().getLong("gui.first.value");
                if (BanknotesPlus.econ.getBalance(playerClicker) >= value) {
                    playerClicker.playSound(playerClicker.getLocation(), Sound.valueOf(main.getConfig().getString("options.buysound")), 3.0F, 0.5F);
                    String formattedValue = String.format("%,d", value);
                    BanknotesPlus.econ.withdrawPlayer(playerClicker, value);
                    playerClicker.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.purchased")).replace("%value%", formattedValue));
                    playerClicker.getInventory().addItem(banknote(value, playerClicker));
                } else {
                    double difference = value - BanknotesPlus.econ.getBalance(playerClicker);
                    playerClicker.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.nomoney").replace("%difference%", Double.toString(difference))));
                }
	            return;
            } if (e.getSlot() == 12) {
                Long value = main.getConfig().getLong("gui.second.value");
                if (BanknotesPlus.econ.getBalance(playerClicker) >= value) {
                    playerClicker.playSound(playerClicker.getLocation(), Sound.valueOf(main.getConfig().getString("options.buysound")), 3.0F, 0.5F);
                    String formattedValue = String.format("%,d", value);
                    BanknotesPlus.econ.withdrawPlayer(playerClicker, value);
                    playerClicker.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.purchased")).replace("%value%", formattedValue));
                    playerClicker.getInventory().addItem(banknote(value, playerClicker));
                } else {
                    double difference = value - BanknotesPlus.econ.getBalance(playerClicker);
                    playerClicker.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.nomoney").replace("%difference%", Double.toString(difference))));
                }
		        return;
	        } if (e.getSlot() == 14) {
                Long value = main.getConfig().getLong("gui.third.value");
                if (BanknotesPlus.econ.getBalance(playerClicker) >= value) {
                    playerClicker.playSound(playerClicker.getLocation(), Sound.valueOf(main.getConfig().getString("options.buysound")), 3.0F, 0.5F);
                    String formattedValue = String.format("%,d", value);
                    BanknotesPlus.econ.withdrawPlayer(playerClicker, value);
                    playerClicker.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.purchased")).replace("%value%", formattedValue));
                    playerClicker.getInventory().addItem(banknote(value, playerClicker));
                } else {
                    double difference = value - BanknotesPlus.econ.getBalance(playerClicker);
                    playerClicker.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.nomoney").replace("%difference%", Double.toString(difference))));
                }
		        return;
	        } if (e.getSlot() == 16) {
                new AnvilGUI(main, playerClicker, "Enter amount...", (player, reply) -> {
                    if (StringUtils.isNumeric(reply)) {
                        Long value = NumberUtils.toLong(reply);
                        String minimum = String.format("%,d", main.getConfig().getLong("options.minimum"));
                        String maximum = String.format("%,d", main.getConfig().getLong("options.maximum"));
                        if (value < main.getConfig().getLong("options.minimum")) {
                            player.sendMessage(ChatColor.RED + "You must withdraw at least $" + minimum);
                            return "Amount must be at least $" + minimum;
                        } else if (value > main.getConfig().getLong("options.maximum")) {
                            player.sendMessage(ChatColor.RED + "You must withdraw less than $" + maximum);
                            return "Amount must be less than $" + maximum;
                        } else {
                            if (BanknotesPlus.econ.getBalance(player) >= value) {
                                player.playSound(player.getLocation(), Sound.valueOf(main.getConfig().getString("options.buysound")), 3.0F, 0.5F);
                                String formattedValue = String.format("%,d", value);
                                BanknotesPlus.econ.withdrawPlayer(player, value);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.purchased")).replace("%value%", formattedValue));
                                player.getInventory().addItem(banknote(value, player));
                                return null;
                            } else {
                                Double difference = value - BanknotesPlus.econ.getBalance(player);
                                long differenceLong = (difference).longValue();
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.nomoney").replace("%difference%", String.format("%,d", differenceLong))));
                                return "Not enough money.";
                            }
                        }
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("messages.error")));
                    return "Enter a valid number.";
                });
            }
        }
    }
}
