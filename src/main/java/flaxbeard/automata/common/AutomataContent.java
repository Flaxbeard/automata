package flaxbeard.automata.common;

import flaxbeard.automata.Automata;
import flaxbeard.automata.common.block.BlockProgrammer;
import flaxbeard.automata.common.codeblock.*;
import flaxbeard.automata.common.entity.EntityAutomaton;
import flaxbeard.automata.common.item.ItemGizmo;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

public class AutomataContent
{
	public static List<Item> items;
	public static List<Block> blocks;

	public static ItemGizmo GIZMO;

	public static void preInit()
	{
		items = new ArrayList<Item>();
		blocks = new ArrayList<Block>();

		int id = 1;
		EntityRegistry.registerModEntity(
				new ResourceLocation(Automata.MODID, "automaton"),
				EntityAutomaton.class,
				"automaton",
				id++,
				Automata.INSTANCE,
				64,
				3,
				true,
				0x996600,
				0x00ff00
		);

        GIZMO = new ItemGizmo();

		new BlockProgrammer();

        CodeBlockRegistry.registerCodeBlock(Automata.MODID, CodeBlockAddPos.class);
        CodeBlockRegistry.registerCodeBlock(Automata.MODID, CodeBlockCurrPos.class);
        CodeBlockRegistry.registerCodeBlock(Automata.MODID, CodeBlockEquals.class);
        CodeBlockRegistry.registerCodeBlock(Automata.MODID, CodeBlockHead.class);
        CodeBlockRegistry.registerCodeBlock(Automata.MODID, CodeBlockMine.class);
        CodeBlockRegistry.registerCodeBlock(Automata.MODID, CodeBlockMove.class);
        CodeBlockRegistry.registerCodeBlock(Automata.MODID, CodeBlockNegTwenty.class);
        CodeBlockRegistry.registerCodeBlock(Automata.MODID, CodeBlockTwenty.class);

    }


}
