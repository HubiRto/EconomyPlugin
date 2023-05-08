package pl.pomoku.economyplugin.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.pomoku.economyplugin.entity.PlayerBalance;
import pl.pomoku.pomokupluginsrepository.commands.CommandInfo;
import pl.pomoku.pomokupluginsrepository.commands.EasyCommand;

import java.util.ArrayList;
import java.util.List;

import static pl.pomoku.economyplugin.EconomyPlugin.playerBalanceService;
import static pl.pomoku.pomokupluginsrepository.text.Text.strToComp;

@CommandInfo(name = "money", requiresPlayer = false)
public class Money extends EasyCommand implements TabCompleter {
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
            if (args.length == 0) {
                playerBalance = playerBalanceService.findPlayerBalanceByPlayer(player);
                player.sendMessage(strToComp("<gray>Stan konta: <green>" + playerBalance.getBalance() + "$"));
            }else if(args.length == 1) {
                if(args[0].equals("top")){
                    List<PlayerBalance> playersBalanceList = playerBalanceService.findTopPlayersByBalance(10);
                    if(playersBalanceList != null) {
                        String BORDER = "<dark_gray>[<red>+</red>]------------[<aqua>TOP</aqua>]------------[<red>+</red>]";
                        player.sendMessage(strToComp(BORDER));
                        for (int i = 0; i < playersBalanceList.size(); i++) {
                            player.sendMessage(strToComp("<gray>" + (i + 1) + ". "
                                    + playersBalanceList.get(i).getPlayerName() + " <dark_gray>- <green>"
                                    + playersBalanceList.get(i).getBalance() + "</green>$"));
                        }
                        player.sendMessage(strToComp(BORDER));
                    }else {
                        player.sendMessage(strToComp("<red>Nie ma graczy!"));
                    }
                }else if(args[0].equals("add")){
                    if(!player.hasPermission("eco.add")) return;

                }
            }
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> tab = new ArrayList<>();
        tab.add("top");
        if(commandSender.hasPermission("eco.add") || commandSender instanceof ConsoleCommandSender) tab.add("add");
        return tab;
    }
}
