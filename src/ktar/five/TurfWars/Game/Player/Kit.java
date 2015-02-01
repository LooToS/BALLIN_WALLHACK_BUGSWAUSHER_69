package ktar.five.TurfWars.Game.Player;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public enum Kit {

	MARKSMAN(false, 2, 2, 0, newIs(Material.BOW, 1)),
	SHREDDER(true, 4, 2, 5000, newIs(Material.BOW, 1)),
	INFILTRATOR(true, 8, 1, 2000, newIs(Material.BOW, 1), newIs(Material.IRON_SWORD, 1));


	protected List<ItemStack> items;
	public int cost;
	public int maxArrows;
	public int oneArrowPerX;
	public boolean canVenture;

	private Kit (boolean canVenture, int oneArrowPerX, int maxArrows, int cost, ItemStack... stack){
		items = Arrays.asList(stack);
		this.canVenture = canVenture;
		this.oneArrowPerX = oneArrowPerX;
		this.maxArrows = maxArrows;
		this.cost = cost;
	}  
	
	public List<ItemStack> getItems(){
		return items;
	}
	
	protected static ItemStack newIs(Material material, int quantity){
		return new ItemStack(material, quantity);
	}
	
	protected static ItemStack newIs(Material material, int quantity, int data){
		return new ItemStack(material, quantity, (short) data);
	}
	
	/*public ItemStack newIs(Team team, int quantity){
		return new ItemStack(Material.STAINED_CLAY, quantity, team.color);
	} 
		Its not really needed...
	*/




}
