package pl.pomoku.economyplugin.manager;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import pl.pomoku.pomokupluginsrepository.items.ItemBuilder;

import java.nio.ByteBuffer;

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
        byte[] amountBytes = intToBytes(amount);
        itemMeta.getPersistentDataContainer().set(createKey(AMOUNT_KEY), PersistentDataType.BYTE_ARRAY, amountBytes);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static boolean isBanknote(ItemStack itemStack) {
        if (itemStack == null) return false;
        if (itemStack.getType() != PAPER) return false;
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return false;
        return meta.getPersistentDataContainer().has(createKey(BANKNOTE_KEY), PersistentDataType.BYTE);
    }

    public static int getBanknoteAmount(ItemStack itemStack) {
        if (isBanknote(itemStack)) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            byte[] amountBytes = itemMeta.getPersistentDataContainer().get(createKey(AMOUNT_KEY), PersistentDataType.BYTE_ARRAY);
            return bytesToInt(amountBytes);
        }
        return 0;
    }

    private static NamespacedKey createKey(String key) {
        return new NamespacedKey(plugin, key);
    }

    private static byte[] intToBytes(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    private static int bytesToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }
}
