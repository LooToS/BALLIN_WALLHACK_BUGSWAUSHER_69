package ktar.five.TurfWars.guiapi.menus.items;

import ktar.five.TurfWars.guiapi.menus.events.ItemClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * A {@link pigeoncraft.gui.menus.items.StaticMenuItem} that closes the {@link pigeoncraft.gui.menus.menus.ItemMenu}.
 */
public class CloseItem extends StaticMenuItem {

    public CloseItem() {
        super(ChatColor.RED + "Close", new ItemStack(Material.REDSTONE_BLOCK));
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        event.setWillClose(true);
    }
}
