package ktar.five.TurfWars.Game.Cooling;

import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TurfEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private UUID person;

	public TurfEvent(UUID person) {
		this.person = person;
	}

	public UUID getUUID() {
		return this.person;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
