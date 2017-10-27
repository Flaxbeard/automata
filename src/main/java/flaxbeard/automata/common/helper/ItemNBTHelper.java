package flaxbeard.automata.common.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemNBTHelper
{
	public static boolean hasTag(ItemStack stack)
	{
		return stack.hasTagCompound();
	}
	
	public static NBTTagCompound getTag(ItemStack stack)
	{
		if (!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
		}
		return stack.getTagCompound();
	}
	
	public static boolean hasKey(ItemStack stack, String name)
	{
		return hasTag(stack) && getTag(stack).hasKey(name);
	}
	
	public static void removeKey(ItemStack stack, String name)
	{
		if (hasKey(stack, name))
		{
			getTag(stack).removeTag(name);
		}
	}

	public static int getInt(ItemStack stack, String name)
	{
		if (hasKey(stack, name))
		{
			return getTag(stack).getInteger(name);
		}
		return 0;
	}

	public static void setInt(ItemStack stack, String name, int value)
	{
		getTag(stack).setInteger(name, value);
	}

	public static void setString(ItemStack stack, String name, String value)
	{
		getTag(stack).setString(name, value);
	}

	public static String getString(ItemStack stack, String name)
	{
		if (hasKey(stack, name))
		{
			return getTag(stack).getString(name);
		}
		return null;
	}
}
