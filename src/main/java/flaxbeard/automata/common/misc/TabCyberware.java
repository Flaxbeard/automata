package flaxbeard.automata.common.misc;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class TabCyberware extends CreativeTabs
{

	public TabCyberware(String label)
	{
		super(label);
	}
	
	@Override
	public ItemStack getTabIconItem()
	{
		return null;
	}
	@Override
	public ItemStack getIconItemStack()
	{
		return new ItemStack(Items.APPLE);
	}

}