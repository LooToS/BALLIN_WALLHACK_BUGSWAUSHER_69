package ktar.five.TurfWars.guiapi.menus.items;

import ktar.five.TurfWars.guiapi.menus.events.ItemClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * A {@link pigeoncraft.gui.menus.items.StaticMenuItem} that opens the {@link pigeoncraft.gui.menus.menus.ItemMenu}'s parent menu if it exists.
 */
public class BackItem extends StaticMenuItem {

    public BackItem() {
        super(ChatColor.RED + "Back", new ItemStack(Material.HOPPER));
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        event.setWillGoBack(true);
    }
}
