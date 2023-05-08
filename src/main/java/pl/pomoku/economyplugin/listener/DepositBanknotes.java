package pl.pomoku.economyplugin.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pl.pomoku.economyplugin.entity.PlayerBalance;
import pl.pomoku.economyplugin.manager.BanknoteUtils;

import static pl.pomoku.economyplugin.EconomyPlugin.playerBalanceService;
import static pl.pomoku.pomokupluginsrepository.text.Text.strToComp;

public class DepositBanknotes implements Listener {
    @EventHandler
    public void deposit(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;

        Player player = event.getPlayer();
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();

        int amount = BanknoteUtils.getBanknoteAmount(itemInMainHand);
        if (amount == 0) return;

        PlayerBalance playerBalance = playerBalanceService.findPlayerBalanceByPlayer(player);
        if (playerBalance == null) return;

        playerBalance.setBalance(playerBalance.getBalance() + amount);
        playerBalanceService.updatePlayerBalance(playerBalance);

        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setItemInMainHand(null);

        player.sendMessage(strToComp("<gray>Wpłacono <green>" + amount + "$</green> na konto."));
    }
}
