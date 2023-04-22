package pl.pomoku.economyplugin.command;

import com.virtame.menusystem.commands_system.CommandInfo;
import com.virtame.menusystem.commands_system.EzCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.pomoku.economyplugin.entity.PlayerBalance;

import static com.virtame.menusystem.text.MiniMes.mmd;
import static pl.pomoku.economyplugin.EconomyPlugin.playerBalanceService;

@CommandInfo(name = "money", requiresPlayer = false)
public class Money extends EzCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        PlayerBalance playerBalance;
        if(!(sender instanceof Player player)){
            if(args.length != 1) return;
            Player soughtPlayer = Bukkit.getPlayer(args[0]);
            if(soughtPlayer == null || !soughtPlayer.isOnline()) return;
            playerBalance = playerBalanceService.findPlayerBalanceByPlayer(soughtPlayer);
            if(playerBalance == null) return;
            sender.sendMessage(mmd("<gray>Gracz <aqua>" + soughtPlayer.getName() + "<aqua> ma na koncie: <green>"
                    + playerBalance.getBalance()));
        }else {
            playerBalance = playerBalanceService.findPlayerBalanceByPlayer(player);
            player.sendMessage(mmd("<gray>Masz na koncie: <green>" + playerBalance.getBalance()));
        }
    }
}
