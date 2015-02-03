package ktar.five.TurfWars.guiapi.menus.items;

import ktar.five.TurfWars.guiapi.menus.events.ItemClickEvent;
import ktar.five.TurfWars.guiapi.menus.menus.ItemMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A {@link pigeoncraft.gui.menus.items.MenuItem} that opens a sub {@link pigeoncraft.gui.menus.menus.ItemMenu}.
 */
public class SubMenuItem extends MenuItem {
    private final JavaPlugin plugin;
    private final ItemMenu menu;

    public SubMenuItem(JavaPlugin plugin, String displayName, ItemStack icon, ItemMenu menu, String... lore) {
        super(displayName, icon, lore);
        this.plugin = plugin;
        this.menu = menu;
    }

    @Override
    public void onItemClick(ItemClickEvent event) {
        event.setWillClose(true);
        final String playerName = event.getPlayer().getName();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                Player p = Bukkit.getPlayerExact(playerName);
                if (p != null) {
                    menu.open(p);
                }
            }
        }, 3);
    }
}
