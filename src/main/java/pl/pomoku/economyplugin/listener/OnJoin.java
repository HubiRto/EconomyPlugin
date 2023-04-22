package pl.pomoku.economyplugin.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.pomoku.economyplugin.entity.PlayerBalance;
import pl.pomoku.economyplugin.entity.TimePlayer;

import static pl.pomoku.economyplugin.EconomyPlugin.*;

public class OnJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerBalance playerBalance = playerBalanceService.findPlayerBalanceByPlayer(player);
        TimePlayer timePlayer = timePlayerService.findByPlayer(player);
        if (playerBalance == null) {
            PlayerBalance newPlayerBalance = PlayerBalance.builder()
                    .playerUUID(player.getUniqueId().toString())
                    .playerName(player.getName())
                    .balance(0.0).build();
            playerBalanceService.addPlayerBalance(newPlayerBalance);
        }
        if(timePlayer == null){
            TimePlayer newTimePlayer = TimePlayer.builder()
                    .collectCounter(0)
                    .lastTimeCollect(System.currentTimeMillis())
                    .playerUUID(player.getUniqueId().toString())
                    .tookAllMoney(false)
                    .build();
            timePlayerService.addTimePlayer(newTimePlayer);
        }
    }
}
