package ktar.five.TurfWars.guiapi.menus.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;
import java.util.List;
import ktar.five.TurfWars.guiapi.menus.events.ItemClickEvent;

/**
 * An Item inside an {@link pigeoncraft.gui.menus.menus.ItemMenu}.
 */
public class MenuItem {
    private final String displayName;
    private final ItemStack icon;
    private final List<String> lore;

    public MenuItem(String displayName, ItemStack icon, String... lore) {
        this.displayName = displayName;
        this.icon = icon;
        this.lore = Arrays.asList(lore);
    }

    /**
     * Gets the display name of the MenuItem.
     *
     * @return The display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the icon of the MenuItem.
     *
     * @return The icon.
     */
    public ItemStack getIcon() {
        return icon;
    }

    /**
     * Gets the lore of the MenuItem.
     *
     * @return The lore.
     */
    public List<String> getLore() {
        return lore;
    }

    /**
     * Gets the ItemStack to be shown to the player.
     *
     * @param player The player.
     * @return The final icon.
     */
    public ItemStack getFinalIcon(Player player) {
        return setNameAndLore(getIcon().clone(), getDisplayName(), getLore());
    }

    /**
     * Called when the MenuItem is clicked.
     *
     * @param event The {@link pigeoncraft.gui.menus.events.ItemClickEvent}.
     */
    public void onItemClick(ItemClickEvent event) {
        // Do nothing by default
    }

    /**
     * Sets the display name and lore of an ItemStack.
     *
     * @param itemStack   The ItemStack.
     * @param displayName The display name.
     * @param lore        The lore.
     * @return The ItemStack.
     */
    public static ItemStack setNameAndLore(ItemStack itemStack, String displayName, List<String> lore) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
