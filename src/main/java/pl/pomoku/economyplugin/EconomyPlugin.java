package pl.pomoku.economyplugin;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class EconomyPlugin extends JavaPlugin {
    public static EconomyPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
    }

}
