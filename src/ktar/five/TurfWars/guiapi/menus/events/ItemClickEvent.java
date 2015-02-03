package ktar.five.TurfWars.guiapi.menus.events;

import org.bukkit.entity.Player;

/**
 * An event called when an Item in the {@link pigeoncraft.gui.menus.menus.ItemMenu} is clicked.
 */
public class ItemClickEvent {
    private Player player;
    private boolean goBack = false;
    private boolean close = false;
    private boolean update = false;

    public ItemClickEvent(Player player) {
        this.player = player;
    }

    /**
     * Gets the player who clicked.
     *
     * @return The player who clicked.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Checks if the {@link pigeoncraft.gui.menus.menus.ItemMenu} will go back to the parent menu.
     *
     * @return True if the {@link pigeoncraft.gui.menus.menus.ItemMenu} will go back to the parent menu, else false.
     */
    public boolean willGoBack() {
        return goBack;
    }

    /**
     * Sets if the {@link pigeoncraft.gui.menus.menus.ItemMenu} will go back to the parent menu.
     *
     * @param goBack If the {@link pigeoncraft.gui.menus.menus.ItemMenu} will go back to the parent menu.
     */
    public void setWillGoBack(boolean goBack) {
        this.goBack = goBack;
        if (goBack) {
            close = false;
            update = false;
        }
    }

    /**
     * Checks if the {@link pigeoncraft.gui.menus.menus.ItemMenu} will close.
     *
     * @return True if the {@link pigeoncraft.gui.menus.menus.ItemMenu} will close, else false.
     */
    public boolean willClose() {
        return close;
    }

    /**
     * Sets if the {@link pigeoncraft.gui.menus.menus.ItemMenu} will close.
     *
     * @param close If the {@link pigeoncraft.gui.menus.menus.ItemMenu} will close.
     */
    public void setWillClose(boolean close) {
        this.close = close;
        if (close) {
            goBack = false;
            update = false;
        }
    }

    /**
     * Checks if the {@link pigeoncraft.gui.menus.menus.ItemMenu} will update.
     *
     * @return True if the {@link pigeoncraft.gui.menus.menus.ItemMenu} will update, else false.
     */
    public boolean willUpdate() {
        return update;
    }

    /**
     * Sets if the {@link pigeoncraft.gui.menus.menus.ItemMenu} will update.
     *
     * @param update If the {@link pigeoncraft.gui.menus.menus.ItemMenu} will update.
     */
    public void setWillUpdate(boolean update) {
        this.update = update;
        if (update) {
            goBack = false;
            close = false;
        }
    }
}
