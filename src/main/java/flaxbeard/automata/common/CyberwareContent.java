package flaxbeard.automata.common;

import flaxbeard.automata.common.block.BlockProgrammer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class CyberwareContent
{
	public static List<Item> items;
	public static List<Block> blocks;

	public static void preInit()
	{
		items = new ArrayList<Item>();
		blocks = new ArrayList<Block>();


		new BlockProgrammer();
	}


}
