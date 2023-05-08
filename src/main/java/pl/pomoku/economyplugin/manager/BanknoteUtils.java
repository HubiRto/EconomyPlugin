package pl.pomoku.economyplugin.manager;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import pl.pomoku.pomokupluginsrepository.items.ItemBuilder;

import static org.bukkit.Material.PAPER;
import static pl.pomoku.economyplugin.EconomyPlugin.plugin;
import static pl.pomoku.pomokupluginsrepository.text.Text.strToComp;

public class BanknoteUtils {
    private static final String BANKNOTE_KEY = "isBanknote";
    private static final String AMOUNT_KEY = "banknoteAmount";

    public static ItemStack create(int amount) {
        ItemStack itemStack = new ItemBuilder(PAPER)
                .displayname(strToComp("<aqua>Banknot <green>" + amount + "$")).build();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(createKey(BANKNOTE_KEY), PersistentDataType.BYTE, (byte) 1);
        itemMeta.getPersistentDataContainer().set(createKey(AMOUNT_KEY), PersistentDataType.INTEGER, amount);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static boolean isBanknot(ItemStack itemStack) {
        if (itemStack == null) return false;
        if (itemStack.getType() != PAPER) return false;
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return false;
        return meta.getPersistentDataContainer().has(createKey(BANKNOTE_KEY), PersistentDataType.BYTE);
    }

    public static int getBanknoteAmount(ItemStack itemStack) {
        if (isBanknot(itemStack)) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            return itemMeta.getPersistentDataContainer().getOrDefault(createKey(AMOUNT_KEY), PersistentDataType.INTEGER, 0);
        }
        return 0;
    }

    private static NamespacedKey createKey(String key) {
        return new NamespacedKey(plugin, key);
    }
}
