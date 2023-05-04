package pl.pomoku.economyplugin.command;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pl.pomoku.pomokupluginsrepository.commands.CommandInfo;
import pl.pomoku.pomokupluginsrepository.commands.EasyCommand;

@CommandInfo(name = "withdraw", requiresPlayer = true)
public class Withdraw extends EasyCommand {
    @Override
    public void execute(Player p, String[] args) {

    }

//    private static boolean isPlayerHaveFreeSlot(Player player){
//        Inventory inventory = player.getInventory();
//        for(int i = 0; i < inventory.getSize(); i++){
//        }
//    }
}
