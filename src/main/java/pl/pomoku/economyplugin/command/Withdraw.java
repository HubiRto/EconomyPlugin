package pl.pomoku.economyplugin.command;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.pomoku.economyplugin.entity.PlayerBalance;
import pl.pomoku.economyplugin.manager.BanknoteUtils;
import pl.pomoku.pomokupluginsrepository.commands.CommandInfo;
import pl.pomoku.pomokupluginsrepository.commands.EasyCommand;
import pl.pomoku.pomokupluginsrepository.items.ItemBuilder;

import static pl.pomoku.economyplugin.EconomyPlugin.playerBalanceService;
import static pl.pomoku.pomokupluginsrepository.text.Text.strToComp;

@CommandInfo(name = "withdraw", requiresPlayer = true)
@SuppressWarnings("unused")
public class Withdraw extends EasyCommand {
    @Override
    public void execute(Player p, String[] args) {
        if (args.length != 1) return;
        if (args[0] == null) return;

        String strMoney = args[0];
        if (!isNumeric(strMoney)) {
            p.sendMessage(strToComp("<red>Nieprawidłowy format kwoty."));
            return;
        }

        int money = Integer.parseInt(strMoney);
        PlayerBalance playerBalance = playerBalanceService.findPlayerBalanceByPlayer(p);
        if (playerBalance == null) {
            p.sendMessage(strToComp("<red>Nie masz konta."));
            return;
        }

        if (playerBalance.getBalance() < money) {
            p.sendMessage(strToComp("<red>Nie masz wystarczająco pieniędzy."));
            return;
        }

        playerBalance.setBalance(playerBalance.getBalance() - money);
        playerBalanceService.updatePlayerBalance(playerBalance);
        p.getInventory().addItem(BanknoteUtils.create(money));
        p.sendMessage(strToComp("<gray>Wypłacono <green>" + money + "$</green> z konta."));
    }

    public static boolean isNumeric(String string) {
        int intValue;
        if (string == null || string.equals("")) {
            System.out.println("String cannot be parsed, it is null or empty.");
            return false;
        }

        try {
            intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Input String cannot be parsed to Integer.");
        }
        return false;
    }
}
