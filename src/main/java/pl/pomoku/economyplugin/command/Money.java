package pl.pomoku.economyplugin.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.pomoku.economyplugin.entity.PlayerBalance;
import pl.pomoku.pomokupluginsrepository.commands.CommandInfo;
import pl.pomoku.pomokupluginsrepository.commands.EasyCommand;

import static pl.pomoku.economyplugin.EconomyPlugin.playerBalanceService;
import static pl.pomoku.pomokupluginsrepository.text.Text.strToComp;

@CommandInfo(name = "money", requiresPlayer = false)
public class Money extends EasyCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        PlayerBalance playerBalance;
        if (!(sender instanceof Player player)) {
            if (args.length != 1) return;
            Player soughtPlayer = Bukkit.getPlayer(args[0]);
            if (soughtPlayer == null || !soughtPlayer.isOnline()) return;
            playerBalance = playerBalanceService.findPlayerBalanceByPlayer(soughtPlayer);
            if (playerBalance == null) return;
            sender.sendMessage(strToComp("<gray>Gracz <aqua>" + soughtPlayer.getName() + "<aqua> ma na koncie: <green>"
                    + playerBalance.getBalance()));
        } else {
            playerBalance = playerBalanceService.findPlayerBalanceByPlayer(player);
            player.sendMessage(strToComp("<gray>Masz na koncie: <green>" + playerBalance.getBalance()));
        }
    }
}
