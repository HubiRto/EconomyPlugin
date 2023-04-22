package pl.pomoku.economyplugin.manager;

import lombok.AllArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

import static pl.pomoku.economyplugin.EconomyPlugin.plugin;

@AllArgsConstructor
public class TimeIsMoneyManager {
    public void run(){
        new BukkitRunnable() {
            @Override
            public void run() {

            }
        }.runTaskLaterAsynchronously(plugin, 5);
    }
}
