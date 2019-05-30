package pw.landon.banknotes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class RedeemEvent implements Listener {

    private BanknotesPlus main;
    public RedeemEvent(BanknotesPlus main) {
        this.main = main;
    }

    @EventHandler
    public void redeem(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (player.getItemInHand().getType().toString().equals(main.getConfig().getString("banknote.item"))) {
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (player.getItemInHand().getItemMeta().getDisplayName().replace("§", "&").equals(main.getConfig().getString("banknote.name"))) {
                    List<String> lore = (player.getItemInHand().getItemMeta().getLore());
                    String value = lore.get(lore.size() - 1);
                    String valueFixed = value.replace("§1", "").replace("§2", "").replace("§3", "").replace("§4", "").replace("§5", "").replace("§6", "").replace("§7", "").replace("§8", "").replace("§9", "").replace("§0", "");
                    int valueLength = valueFixed.length();
                    StringBuilder valueEconString = new StringBuilder("");
                    for (int i = 0; i < valueLength; i++) {
                        Character character = valueFixed.charAt(i);
                        if (Character.isDigit(character)) {
                            valueEconString.append(character);
                        }
                    }
                    int valueEcon = Integer.parseInt(valueEconString.toString());
                    main.econ.depositPlayer(player, valueEcon);
                    if (player.getItemInHand().getAmount() > 1) {
                        player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                    } else {
                        player.setItemInHand(null);
                    }
                    String redeemedMessage = main.getConfig().getString("messages.redeemed").replace("%value%", String.format("%,d", valueEcon));
                    player.playSound(player.getLocation(), Sound.valueOf(main.getConfig().getString("options.sound")), 3.0F, 0.5F);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', redeemedMessage));
                }
            }
        }
    }
}
