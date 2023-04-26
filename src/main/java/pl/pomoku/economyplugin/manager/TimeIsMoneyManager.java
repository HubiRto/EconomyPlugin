package pl.pomoku.economyplugin.manager;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.pomoku.economyplugin.entity.PlayerBalance;
import pl.pomoku.economyplugin.entity.TimePlayer;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import static pl.pomoku.economyplugin.EconomyPlugin.*;
import static pl.pomoku.pomokupluginsrepository.text.Text.strToComp;

@AllArgsConstructor
public class TimeIsMoneyManager {
    public void runTimer(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                if (calendar.get(Calendar.HOUR_OF_DAY) == 0 && calendar.get(Calendar.MINUTE) == 0) {
                    List<TimePlayer> timePlayers = timePlayerService.findAll();
                    timePlayers.stream().filter(TimePlayer::isTookAllMoney)
                            .forEach(timePlayer -> timePlayer.setTookAllMoney(false));
                    timePlayerService.updateAll(timePlayers);
                }
            }
        }, 0, 60000);
    }

    public void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                List<TimePlayer> timePlayers = finaAllWithFalse();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (TimePlayer timePlayer : timePlayers) {
                        if (timePlayer.getPlayerUUID().equals(player.getUniqueId().toString())) {
                            if (currentTime > timePlayer.getLastTimeCollect() + 1000 * 60 * 5) {
                                if (timePlayer.getCollectCounter() + 1 == 20) {
                                    timePlayer.setTookAllMoney(true);
                                    timePlayer.setCollectCounter(0);
                                    timePlayer.setLastTimeCollect(0);
                                    player.sendMessage(strToComp("Nie otrzymasz już więcej nagród dziś."));
                                } else {
                                    timePlayer.setCollectCounter(timePlayer.getCollectCounter() + 1);
                                    timePlayer.setLastTimeCollect(currentTime);
                                    PlayerBalance playerBalance = playerBalanceService.findPlayerBalanceByPlayer(player);
                                    playerBalance.setBalance(playerBalance.getBalance() + 5);
                                    playerBalanceService.updatePlayerBalance(playerBalance);
                                    player.sendMessage(strToComp("Otrzymałeś 5$ za 5 min gry."));
                                }
                            }
                        }
                    }
                }
                timePlayerService.updateAll(timePlayers);
            }
        }.runTaskTimer(plugin, 60, 0);
    }
    private List<TimePlayer> finaAllWithFalse(){
        return timePlayerService.findAll().stream()
                .filter(e -> !e.isTookAllMoney()).collect(Collectors.toList());
    }
}
