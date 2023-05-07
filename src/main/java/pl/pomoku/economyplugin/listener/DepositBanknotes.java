package pl.pomoku.economyplugin.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import pl.pomoku.economyplugin.entity.PlayerBalance;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.Material.PAPER;
import static pl.pomoku.economyplugin.EconomyPlugin.playerBalanceService;
import static pl.pomoku.pomokupluginsrepository.text.Text.strToComp;

public class DepositBanknotes implements Listener {
    private static final Pattern BANKNOTE_PATTERN = Pattern.compile("^Banknot (\\d+)\\$$");
    @EventHandler
    public void deposit(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;

        Player player = event.getPlayer();
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if (itemInMainHand.getType() != PAPER) return;

        Component displayName = itemInMainHand.getItemMeta().displayName();

        if (displayName == null) return;
        String displayNameString = PlainTextComponentSerializer.plainText().serialize(displayName);
        System.out.println(displayNameString);

        Matcher matcher = BANKNOTE_PATTERN.matcher(displayNameString);
        if (!matcher.matches()) return;
        String amountString = matcher.group(1);

        int amount;

        try {
            amount = Integer.parseInt(amountString);
        } catch (NumberFormatException e) {
            player.sendMessage(strToComp("Niepoprawny format kwoty banknotu."));
            return;
        }

        PlayerBalance playerBalance = playerBalanceService.findPlayerBalanceByPlayer(player);
        if(playerBalance == null) return;

        playerBalance.setBalance(playerBalance.getBalance() + amount);
        playerBalanceService.updatePlayerBalance(playerBalance);

        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setItemInMainHand(null);

        player.sendMessage(strToComp("<gray>Wpłacono <green>" + amount + "</green> na konto."));
    }
}
