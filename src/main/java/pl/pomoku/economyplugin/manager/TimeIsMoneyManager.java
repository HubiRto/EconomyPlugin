package pl.pomoku.economyplugin.manager;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import pl.pomoku.economyplugin.model.Time;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class TimeIsMoneyManager {
    private Map<Player, Time> timeMap;
    private List<Player> tookAllDayAwards;

    public void addPlayerToSystem(Player player){
        if(this.tookAllDayAwards.contains(player)) return;
    }

    public void removePlayerFromSystem(Player player){
        timeMap.remove(player);
    }
}
