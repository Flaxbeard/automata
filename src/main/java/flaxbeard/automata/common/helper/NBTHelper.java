package flaxbeard.automata.common.helper;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class NBTHelper
{
	public static NBTTagCompound writeBlockPos(NBTTagCompound tag, String name, BlockPos pos)
	{
		NBTTagCompound posTag = new NBTTagCompound();
		posTag.setInteger("x", pos.getX());
		posTag.setInteger("y", pos.getY());
		posTag.setInteger("z", pos.getZ());
		tag.setTag(name, posTag);
		return tag;
	}
	
	public static BlockPos readBlockPos(NBTTagCompound tag, String name)
	{
		if (tag.hasKey(name))
		{
			NBTTagCompound posTag = tag.getCompoundTag(name);
			if (posTag != null && posTag.hasKey("x") && posTag.hasKey("y") && posTag.hasKey("z"))
			{
				return new BlockPos(
						posTag.getInteger("x"),
						posTag.getInteger("y"),
						posTag.getInteger("z"));
			}
		}
		return null;
	}
}
