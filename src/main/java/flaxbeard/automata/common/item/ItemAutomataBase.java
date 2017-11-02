package flaxbeard.automata.common.item;

import flaxbeard.automata.Automata;
import flaxbeard.automata.common.AutomataContent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemAutomataBase extends Item
{
	protected String[] subNames;

	public ItemAutomataBase(String name, int stackSize, String... subNames)
	{
		this.setRegistryName(name);
		this.setUnlocalizedName(Automata.MODID + "." + name);

		this.setCreativeTab(Automata.creativeTab);

		this.subNames = subNames;

		this.setHasSubtypes(this.subNames.length > 0);
		this.setMaxDamage(0);

		this.setMaxStackSize(stackSize);

		AutomataContent.items.add(this);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	{
		int damage = itemstack.getItemDamage();
		if (damage >= subNames.length)
		{
			return super.getUnlocalizedName();
		}
		return super.getUnlocalizedName(itemstack) + "." + subNames[damage];
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		if (subNames.length == 0)
		{
			list.add(new ItemStack(this));
		}
		for (int i = 0; i < subNames.length; i++)
		{
			list.add(new ItemStack(this, 1, i));
		}
	}
}
