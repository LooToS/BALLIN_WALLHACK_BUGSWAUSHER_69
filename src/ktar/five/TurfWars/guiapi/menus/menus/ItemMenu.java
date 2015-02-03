package ktar.five.TurfWars.guiapi.menus.menus;

import ktar.five.TurfWars.Main;
import ktar.five.TurfWars.guiapi.menus.MenuListener;
import ktar.five.TurfWars.guiapi.menus.events.ItemClickEvent;
import ktar.five.TurfWars.guiapi.menus.items.MenuItem;
import ktar.five.TurfWars.guiapi.menus.items.StaticMenuItem;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A Menu controlled by ItemStacks in an Inventory.
 */
public class ItemMenu {
    private JavaPlugin plugin;
    private String name;
    private Size size;
    private MenuItem[] items;
    private ItemMenu parent;

    /**
     * The {@link pigeoncraft.gui.menus.items.StaticMenuItem} that appears in empty slots if {@link pigeoncraft.gui.menus.menus.ItemMenu#fillEmptySlots()} is called.
     */
    @SuppressWarnings("deprecation")
    private static final MenuItem EMPTY_SLOT_ITEM = new StaticMenuItem(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData()));

    /**
     * Creates an {@link pigeoncraft.gui.menus.menus.ItemMenu}.
     *
     * @param name   The name of the inventory.
     * @param size   The {@link pigeoncraft.gui.menus.menus.ItemMenu.Size} of the inventory.
     * @param plugin The {@link org.bukkit.plugin.java.JavaPlugin} instance.
     * @param parent The ItemMenu's parent.
     */
    public ItemMenu(String name, Size size, JavaPlugin plugin, ItemMenu parent) {
        this.plugin = plugin;
        this.name = name;
        this.size = size;
        this.items = new MenuItem[size.getSize()];
        this.parent = parent;
    }

    /**
     * Creates an {@link pigeoncraft.gui.menus.menus.ItemMenu} with no parent.
     *
     * @param name   The name of the inventory.
     * @param size   The {@link pigeoncraft.gui.menus.menus.ItemMenu.Size} of the inventory.
     * @param plugin The Plugin instance.
     */
    public ItemMenu(String name, Size size, JavaPlugin plugin) {
        this(name, size, plugin, null);
    }

    /**
     * Gets the name of the {@link pigeoncraft.gui.menus.menus.ItemMenu}.
     *
     * @return The {@link pigeoncraft.gui.menus.menus.ItemMenu}'s name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the {@link pigeoncraft.gui.menus.menus.ItemMenu.Size} of the {@link pigeoncraft.gui.menus.menus.ItemMenu}.
     *
     * @return The {@link pigeoncraft.gui.menus.menus.ItemMenu}'s {@link pigeoncraft.gui.menus.menus.ItemMenu.Size}.
     */
    public Size getSize() {
        return size;
    }

    /**
     * Checks if the {@link pigeoncraft.gui.menus.menus.ItemMenu} has a parent.
     *
     * @return True if the {@link pigeoncraft.gui.menus.menus.ItemMenu} has a parent, else false.
     */
    public boolean hasParent() {
        return parent != null;
    }

    /**
     * Gets the parent of the {@link pigeoncraft.gui.menus.menus.ItemMenu}.
     *
     * @return The {@link pigeoncraft.gui.menus.menus.ItemMenu}'s parent.
     */
    public ItemMenu getParent() {
        return parent;
    }

    /**
     * Sets the parent of the {@link pigeoncraft.gui.menus.menus.ItemMenu}.
     *
     * @param parent The {@link pigeoncraft.gui.menus.menus.ItemMenu}'s parent.
     */
    public void setParent(ItemMenu parent) {
        this.parent = parent;
    }

    /**
     * Sets the {@link pigeoncraft.gui.menus.items.MenuItem} of a slot.
     *
     * @param position The slot position.
     * @param menuItem The {@link pigeoncraft.gui.menus.items.MenuItem}.
     * @return The {@link pigeoncraft.gui.menus.menus.ItemMenu}.
     */
    public ItemMenu setItem(int position, MenuItem menuItem) {
        items[position] = menuItem;
        return this;
    }
    
    /**
     * Sets the {@link pigeoncraft.gui.menus.items.MenuItem} of a slot.
     *
     * @param menuItem The {@link pigeoncraft.gui.menus.items.MenuItem}.
     * @return The {@link pigeoncraft.gui.menus.menus.ItemMenu}.
     */
    public ItemMenu addItem(MenuItem menuItem) {
    	 for (int i = 0; i < items.length; i++) {
             if (items[i] == null) {
                 items[i] = menuItem;
                 return this;
             }
         }
        return this;
    }

    /**
     * Fills all empty slots in the {@link pigeoncraft.gui.menus.menus.ItemMenu} with a certain {@link pigeoncraft.gui.menus.items.MenuItem}.
     *
     * @param menuItem The {@link pigeoncraft.gui.menus.items.MenuItem}.
     * @return The {@link pigeoncraft.gui.menus.menus.ItemMenu}.
     */
    public ItemMenu fillEmptySlots(MenuItem menuItem) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                items[i] = menuItem;
            }
        }
        return this;
    }

    /**
     * Fills all empty slots in the {@link pigeoncraft.gui.menus.menus.ItemMenu} with the default empty slot item.
     *
     * @return The {@link pigeoncraft.gui.menus.menus.ItemMenu}.
     */
    public ItemMenu fillEmptySlots() {
        return fillEmptySlots(EMPTY_SLOT_ITEM);
    }

    /**
     * Opens the {@link pigeoncraft.gui.menus.menus.ItemMenu} for a player.
     *
     * @param player The player.
     */
    public void open(Player player) {
        if (!MenuListener.getInstance().isRegistered(plugin)) {
            MenuListener.getInstance().register(plugin);
        }
        Inventory inventory = Bukkit.createInventory(new MenuHolder(this, Bukkit.createInventory(player, size.getSize())), size.getSize(), name);
        apply(inventory, player);
        player.openInventory(inventory);
    }

    /**
     * Updates the {@link pigeoncraft.gui.menus.menus.ItemMenu} for a player.
     *
     * @param player The player to update the {@link pigeoncraft.gui.menus.menus.ItemMenu} for.
     */
    public void update(Player player) {
        if (player.getOpenInventory() != null) {
            Inventory inventory = player.getOpenInventory().getTopInventory();
            if (inventory.getHolder() instanceof MenuHolder && ((MenuHolder) inventory.getHolder()).getMenu().equals(this)) {
                apply(inventory, player);
                player.updateInventory();
            }
        }
    }

    /**
     * Applies the {@link pigeoncraft.gui.menus.menus.ItemMenu} for a player to an Inventory.
     *
     * @param inventory The Inventory.
     * @param player    The Player.
     */
    private void apply(Inventory inventory, Player player) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                inventory.setItem(i, items[i].getFinalIcon(player));
            }
        }
    }

    /**
     * Handles InventoryClickEvents for the {@link pigeoncraft.gui.menus.menus.ItemMenu}.
     */
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClick() == ClickType.LEFT) {
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < size.getSize() && items[slot] != null) {
                Player player = (Player) event.getWhoClicked();
                ItemClickEvent itemClickEvent = new ItemClickEvent(player);
                items[slot].onItemClick(itemClickEvent);
                if (itemClickEvent.willUpdate()) {
                    update(player);
                } else {
                	if (plugin == null){
                		plugin = Main.instance;
                	}
                    player.updateInventory();
                    if (itemClickEvent.willClose() || itemClickEvent.willGoBack()) {
                        final String playerName = player.getName();
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            public void run() {
                                Player p = Bukkit.getPlayerExact(playerName);
                                if (p != null) {
                                    p.closeInventory();
                                }
                            }
                        }, 1);
                    }
                    if (itemClickEvent.willGoBack() && hasParent()) {
                        final String playerName = player.getName();
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            public void run() {
                                Player p = Bukkit.getPlayerExact(playerName);
                                if (p != null) {
                                    parent.open(p);
                                }
                            }
                        }, 3);
                    }
                }
            }
        }
    }

    /**
     * Destroys the {@link pigeoncraft.gui.menus.menus.ItemMenu}.
     */
    public void destroy() {
        plugin = null;
        name = null;
        size = null;
        items = null;
        parent = null;
    }

    /**
     * Possible sizes of an {@link pigeoncraft.gui.menus.menus.ItemMenu}.
     */
    public enum Size {
        ONE_LINE(9),
        TWO_LINE(18),
        THREE_LINE(27),
        FOUR_LINE(36),
        FIVE_LINE(45),
        SIX_LINE(54);

        private final int size;

        private Size(int size) {
            this.size = size;
        }

        /**
         * Gets the {@link pigeoncraft.gui.menus.menus.ItemMenu.Size}'s amount of slots.
         *
         * @return The amount of slots.
         */
        public int getSize() {
            return size;
        }

        /**
         * Gets the required {@link pigeoncraft.gui.menus.menus.ItemMenu.Size} for an amount of slots.
         *
         * @param slots The amount of slots.
         * @return The required {@link pigeoncraft.gui.menus.menus.ItemMenu.Size}.
         */
        public static Size fit(int slots) {
            if (slots < 10) {
                return ONE_LINE;
            } else if (slots < 19) {
                return TWO_LINE;
            } else if (slots < 28) {
                return THREE_LINE;
            } else if (slots < 37) {
                return FOUR_LINE;
            } else if (slots < 46) {
                return FIVE_LINE;
            } else {
                return SIX_LINE;
            }
        }
    }
}
