package pl.pomoku.economyplugin;

import com.virtame.menusystem.commands_system.EzCommand;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.pomoku.economyplugin.service.PlayerBalanceService;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

@Getter
public final class EconomyPlugin extends JavaPlugin {
    private static AnnotationConfigApplicationContext applicationContext;
    public static EconomyPlugin plugin;
    public static PlayerBalanceService playerBalanceService;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();

        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.scan("pl.pomoku.economyplugin");
        applicationContext.refresh();

        loadListenersAndCommands();
    }

    @Override
    public void onDisable() {
        applicationContext.close();
    }

    private void loadListenersAndCommands() {
        String packageName = getClass().getPackage().getName();
        for (Class<?> clazz : new Reflections(packageName + ".listeners").getSubTypesOf(Listener.class)) {
            try {
                Listener listener = (Listener) clazz.getDeclaredConstructor().newInstance();
                getServer().getPluginManager().registerEvents(listener, this);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        for (Class<? extends EzCommand> clazz : new Reflections(packageName + ".commands").getSubTypesOf(EzCommand.class)) {
            try {
                EzCommand pluginCommand = clazz.getDeclaredConstructor().newInstance();
                Objects.requireNonNull(getCommand(pluginCommand.getCommandInfo().name())).setExecutor(pluginCommand);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

}
