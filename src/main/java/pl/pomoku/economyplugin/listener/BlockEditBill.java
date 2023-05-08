package pl.pomoku.economyplugin.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import pl.pomoku.economyplugin.manager.BanknoteUtils;

import static org.bukkit.Material.AIR;

public class BlockEditBill implements Listener {
    @EventHandler
    public void edit(PrepareAnvilEvent event) {
        AnvilInventory anvilInventory = event.getInventory();
        ItemStack first = anvilInventory.getFirstItem();
        ItemStack result = anvilInventory.getResult();
        if (!BanknoteUtils.isBanknote(first)) return;
        if (first == result) return;
        event.setResult(new ItemStack(AIR));
    }
}
