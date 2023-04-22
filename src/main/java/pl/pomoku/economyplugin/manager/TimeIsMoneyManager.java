package pl.pomoku.economyplugin.manager;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.pomoku.economyplugin.entity.PlayerBalance;
import pl.pomoku.economyplugin.entity.TimePlayer;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static com.virtame.menusystem.text.MiniMes.mmd;
import static pl.pomoku.economyplugin.EconomyPlugin.*;

@AllArgsConstructor
public class TimeIsMoneyManager {
    public void run(){
        new BukkitRunnable() {
            @Override
            public void run() {
                if(LocalTime.now().isAfter(LocalTime.of(0,0,0))){
                    List<TimePlayer> timePlayers = timePlayerService.findAll();
                    timePlayers.stream().filter(TimePlayer::isTookAllMoney)
                            .forEach(timePlayer -> timePlayer.setTookAllMoney(false));
                    timePlayerService.updateAll(timePlayers);
                }

                long currentTime = System.currentTimeMillis();
                Set<TimePlayer> timePlayers = timePlayerService.findAllByTookAllMoneyIsFalse();
                for(Player player : Bukkit.getOnlinePlayers()){
                    for(TimePlayer timePlayer : timePlayers){
                        if(timePlayer.getPlayerUUID().equals(player.getUniqueId().toString())) {
                            if(currentTime > timePlayer.getLastTimeCollect() + 1000 * 60 * 5){
                                if(timePlayer.getCollectCounter() + 1 == 20) {
                                    timePlayer.setTookAllMoney(true);
                                    timePlayer.setCollectCounter(0);
                                    timePlayer.setLastTimeCollect(0);
                                    player.sendMessage(mmd("Nie otrzymasz już więcej nagród dziś."));
                                }else {
                                    timePlayer.setCollectCounter(timePlayer.getCollectCounter() + 1);
                                    timePlayer.setLastTimeCollect(currentTime);
                                    PlayerBalance playerBalance = playerBalanceService.findPlayerBalanceByPlayer(player);
                                    playerBalance.setBalance(playerBalance.getBalance() + 50);
                                    player.sendMessage(mmd("Otrzymałeś 50 $ za 5 min gry."));
                                }
                                timePlayerService.updateTimePlayer(timePlayer);
                            }
                        }
                    }
                }
            }
        }.runTaskLaterAsynchronously(plugin, 5);
    }
}
