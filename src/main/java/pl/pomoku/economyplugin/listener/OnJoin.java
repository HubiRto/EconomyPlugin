package pl.pomoku.economyplugin.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.pomoku.economyplugin.entity.PlayerBalance;

import static pl.pomoku.economyplugin.EconomyPlugin.playerBalanceService;

public class OnJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerBalance playerBalance = playerBalanceService.findPlayerBalanceByPlayer(player);
        if (playerBalance != null) {
            //TODO: Add player to TimeIsMoneySystem
        } else {
            PlayerBalance newPlayerBalance = PlayerBalance.builder()
                    .playerUUID(player.getUniqueId().toString())
                    .playerName(player.getName())
                    .balance(0.0).build();
            playerBalanceService.addPlayerBalance(newPlayerBalance);
        }
    }
}
