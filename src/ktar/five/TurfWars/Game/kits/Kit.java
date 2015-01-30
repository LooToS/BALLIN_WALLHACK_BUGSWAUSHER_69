package ktar.five.TurfWars.Game.kits;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class Kit {

	protected List<ItemStack> items;
	public int cost;
	public int maxArrows;
	public int oneArrowPerX;
	public boolean canVenture;
	
	protected Kit (boolean canVenture, int oneArrowPerX, int maxArrows, int cost, ItemStack... stack){
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
