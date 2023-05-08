package pl.pomoku.economyplugin.command;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
@SuppressWarnings("unused")
public class Money extends EasyCommand implements TabCompleter {
    private final static String TOP_BORDER = "<dark_gray>[<red>+</red>]------------[<aqua>TOP</aqua>]------------[<red>+</red>]";

    /**
     * Wykonuje operację w zależności od rodzaju nadawcy komendy.
     *
     * @param sender obiekt reprezentujący nadawcę komendy
     * @param args   argumenty komendy
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            executeAsConsole(sender, args);
        } else {
            executeAsPlayer(player, args);
        }
    }

    /**
     * Wykonuje operacje dla nadawcy będącego konsolą.
     *
     * @param sender obiekt reprezentujący nadawcę komendy
     * @param args   argumenty komendy
     */
    private void executeAsConsole(CommandSender sender, String[] args) {
        if (args.length == 1) {
            handleConsoleBalanceCommand(sender, args);
        } else if (args.length == 3) {
            handleConsoleMoneyCommand(args, sender);
        }
    }

    /**
     * Wykonuje operację dla nadawcy będącego graczem.
     *
     * @param player obiekt reprezentujący nadawcę komendy będącego graczem
     * @param args   argumenty komendy
     */
    private void executeAsPlayer(Player player, String[] args) {
        if (args.length == 0) {
            handlePlayerBalanceCommand(player);
        } else if (args.length == 1) {
            handlePlayerOneArg(args, player);
        } else if (args.length == 3) {
            handlePlayerMoneyCommand(args, player);
        }
    }

    /**
     * Obsługuje operacje dla gracza, gdy ma tylko jeden argument.
     *
     * @param args   argumenty komendy
     * @param player obiekt reprezentujący nadawcę komendy (gracza)
     */
    private void handlePlayerOneArg(String[] args, Player player) {
        switch (args[0]) {
            case "top" -> handlePlayerTopCommand(player, args);
            default -> handlePlayerOtherBalanceCommand(player, args);
        }
    }

    /**
     * Obsługuje komendę związane z wyświetleniem stanu konta dla innego gracza.
     *
     * @param player obiekt reprezentujący nadawcę komendy (gracza)
     * @param args   argumenty komendy
     */
    private void handlePlayerOtherBalanceCommand(Player player, String[] args) {
        if (!player.hasPermission("eco.balance.others")) return;
        String targetName = args[0];
        PlayerBalance targetBalance = findPlayerBalance(targetName, player);
        Component text = strToComp("<gray>Stan konta gracza " + targetName
                + ": <green>" + targetBalance.getBalance() + "$");
        player.sendMessage(text);
    }

    /**
     * Obsługuje komendę związane z wyświetleniem stanu konta dla konsoli.
     *
     * @param sender obiekt reprezentujący nadawcę komendy (konsolę)
     * @param args   argumenty komendy
     */
    private void handleConsoleBalanceCommand(CommandSender sender, String[] args) {
        PlayerBalance playerBalance = findPlayerBalance(args[0], sender);
        Component text = strToComp("<gray>Gracz <aqua>" + args[0] + "<aqua> ma na koncie: <green>"
                + playerBalance.getBalance());
        sender.sendMessage(text);
    }

    /**
     * Obsługuje komendę związane z dodawaniem lub usuwaniem pieniędzy dla konsoli.
     *
     * @param args   argumenty komendy
     * @param sender obiekt reprezentujący nadawcę komendy (konsolę)
     */
    private void handleConsoleMoneyCommand(String[] args, CommandSender sender) {
        if (args[0].equals("add")) {
            addMoney(args, sender);
        } else if (args[0].equals("remove")) {
            removeMoney(args, sender);
        }
    }

    /**
     * Obsługuje komendę związane z wyświetleniem stanu konta dla gracza.
     *
     * @param player obiekt reprezentujący nadawcę komendy (gracza)
     */
    private void handlePlayerBalanceCommand(Player player) {
        PlayerBalance playerBalance = playerBalanceService.findPlayerBalanceByPlayer(player);
        player.sendMessage(strToComp("<gray>Stan konta: <green>" + playerBalance.getBalance() + "$"));
    }

    /**
     * Obsługuje komendę związane z wyświetleniem najlepszych graczy pod względem stanu konta dla gracza.
     *
     * @param player obiekt reprezentujący nadawcę komendy (gracza)
     * @param args   argumenty komendy
     */
    private void handlePlayerTopCommand(Player player, String[] args) {
        if (!player.hasPermission("eco.top")) return;
        if (args[0].equals("top")) {
            List<PlayerBalance> playersBalanceList = playerBalanceService.findTopPlayersByBalance(10);
            if (playersBalanceList != null) {
                player.sendMessage(strToComp(TOP_BORDER));
                for (int i = 0; i < playersBalanceList.size(); i++) {
                    player.sendMessage(strToComp("<gray>" + (i + 1) + ". "
                            + playersBalanceList.get(i).getPlayerName() + " <dark_gray>- <green>"
                            + playersBalanceList.get(i).getBalance() + "</green>$"));
                }
                player.sendMessage(strToComp(TOP_BORDER));
            } else {
                player.sendMessage(strToComp("<red>Nie ma graczy!"));
            }
        }
    }

    /**
     * Obsługuje komendę związane z dodawaniem lub usuwaniem pieniędzy dla gracza.
     *
     * @param args   argumenty komendy
     * @param player obiekt reprezentujący nadawcę komendy (gracza)
     */
    private void handlePlayerMoneyCommand(String[] args, Player player) {
        if (args[0].equals("add")) {
            if (!player.hasPermission("eco.add")) return;
            addMoney(args, player);
        } else if (args[0].equals("remove")) {
            if (!player.hasPermission("eco.remove")) return;
            removeMoney(args, player);
        }
    }

    /**
     * Dodaje określoną kwotę pieniędzy do bilansu gracza.
     *
     * @param args   tablica argumentów, gdzie args[1] to nazwa gracza, a args[2] to kwota
     * @param sender obiekt reprezentujący nadawcę komendy
     */
    private static void addMoney(String[] args, CommandSender sender) {
        processTransaction(args, sender, (targetBalance, amount) -> {
            targetBalance.setBalance(targetBalance.getBalance() + amount);
            updateBalanceAndSendMessage(targetBalance, amount,
                    "<gray>Dodano <green>" + amount + "$</green> graczu <aqua>" + args[1], sender);
        });
    }

    /**
     * Odejmuje określoną kwotę pieniędzy z bilansu gracza.
     *
     * @param args   tablica argumentów, gdzie args[1] to nazwa gracza, a args[2] to kwota
     * @param sender obiekt reprezentujący nadawcę komendy
     */
    private static void removeMoney(String[] args, CommandSender sender) {
        processTransaction(args, sender, (targetBalance, amount) -> {
            targetBalance.setBalance(targetBalance.getBalance() - amount);
            updateBalanceAndSendMessage(targetBalance, amount,
                    "<gray>Zabrano <red>" + amount + "$</red> graczu <aqua>" + args[1], sender);
        });
    }

    /**
     * Przetwarza transakcję, sprawdzając poprawność danych i wykonując określoną operację na bilansie gracza.
     *
     * @param args    tablica argumentów, gdzie args[1] to nazwa gracza, a args[2] to kwota
     * @param sender  obiekt reprezentujący nadawcę komendy
     * @param handler obiekt obsługujący operację na bilansie gracza
     */
    private static void processTransaction(String[] args, CommandSender sender, TransactionHandler handler) {
        String target = args[1];
        PlayerBalance targetBalance = findPlayerBalance(target, sender);
        if (targetBalance == null) {
            return;
        }
        int amount = parseAmount(args[2], sender);
        if (amount == -1) {
            return;
        }
        handler.handle(targetBalance, amount);
    }

    /**
     * Interfejs funkcyjny do obsługi operacji na bilansie gracza.
     */
    private interface TransactionHandler {
        /**
         * Wykonuje operację na bilansie gracza.
         *
         * @param targetBalance bilans gracza
         * @param amount        kwota do przetworzenia
         */
        void handle(PlayerBalance targetBalance, int amount);
    }

    /**
     * Znajduje bilans gracza na podstawie podanej nazwy gracza.
     *
     * @param playerName nazwa gracza
     * @param sender     obiekt reprezentujący nadawcę komendy
     * @return bilans gracza lub null, jeśli nie znaleziono gracza
     */
    private static PlayerBalance findPlayerBalance(String playerName, CommandSender sender) {
        PlayerBalance targetBalance = playerBalanceService.findPlayerBalanceByPlayerName(playerName);
        if (targetBalance == null) {
            sender.sendMessage(strToComp("<red>Brak danych o koncie gracza: <gray>" + playerName));
        }
        return targetBalance;
    }

    /**
     * Parsuje podaną wartość jako kwotę pieniędzy.
     *
     * @param amountStr wartość do sparsowania
     * @param sender    obiekt reprezentujący nadawcę komendy
     * @return sparsowana kwota lub -1, jeśli format jest nieprawidłowy
     */
    private static int parseAmount(String amountStr, CommandSender sender) {
        if (!Withdraw.isNumeric(amountStr)) {
            sender.sendMessage(strToComp("<red>Nieprawidłowy format kwoty."));
            return -1;
        }
        return Integer.parseInt(amountStr);
    }

    /**
     * Aktualizuje bilans gracza i wysyła wiadomość do nadawcy.
     *
     * @param targetBalance bilans gracza
     * @param amount        kwota
     * @param message       wiadomość do wysłania
     * @param sender        obiekt reprezentujący nadawcę komendy
     */
    private static void updateBalanceAndSendMessage(
            PlayerBalance targetBalance,
            int amount,
            String message,
            CommandSender sender
    ) {
        playerBalanceService.updatePlayerBalance(targetBalance);
        sender.sendMessage(strToComp(message));
    }

    /**
     * Obsługuje podpowiedzi dla komendy w zależności od nadawcy.
     *
     * @param commandSender obiekt reprezentujący nadawcę komendy
     * @param command       komenda
     * @param s             alias komendy
     * @param args          argumenty komendy
     * @return lista podpowiedzi dla komendy
     */
    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender commandSender,
            @NotNull Command command,
            @NotNull String s,
            @NotNull String[] args
    ) {
        if (commandSender instanceof Player player) {
            return tabForPlayer(args, player);
        } else {
            return tabForConsole(args);
        }
    }

    /**
     * Generuje podpowiedzi dla komendy dla nadawcy będącego konsolą.
     *
     * @param args argumenty komendy
     * @return lista podpowiedzi dla komendy
     */
    private static List<String> tabForConsole(String[] args) {
        switch (args.length) {
            case 1 -> {
                return addAllPlayersNamesToTab(List.of("top", "add", "remove"));
            }
            case 2 -> {
                switch (args[0]) {
                    case "add", "remove" -> {
                        return allPlayersNames();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Generuje podpowiedzi dla komendy dla nadawcy będącego graczem.
     *
     * @param args   argumenty komendy
     * @param player gracz będący nadawcą komendy
     * @return lista podpowiedzi dla komendy
     */
    private static List<String> tabForPlayer(String[] args, Player player) {
        List<String> tab = new ArrayList<>();
        switch (args.length) {
            case 1 -> {
                tab.add("top");
                if (player.hasPermission("eco.add")) tab.add("add");
                if (player.hasPermission("eco.remove")) tab.add("remove");
                if (player.hasPermission("eco.balance.others")) addAllPlayersNamesToTab(tab);
                return tab;
            }
            case 2 -> {
                if ((args[0].equals("add") && player.hasPermission("eco.add"))
                        || (args[0].equals("remove") && player.hasPermission("eco.remove"))) {
                    addAllPlayersNamesToTab(tab);
                }
            }
        }
        return null;
    }

    /**
     * Dodaje wszystkie nazwy graczy do listy podpowiedzi.
     *
     * @param tab lista podpowiedzi
     * @return lista podpowiedzi z dodanymi nazwami graczy
     */
    private static List<String> addAllPlayersNamesToTab(List<String> tab) {
        tab.addAll(allPlayersNames());
        return tab;
    }

    /**
     * Zwraca listę wszystkich nazw graczy online.
     *
     * @return lista nazw graczy
     */
    private static List<String> allPlayersNames() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }
}
