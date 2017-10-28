package flaxbeard.automata.common;

import flaxbeard.automata.Automata;
import flaxbeard.automata.client.gui.codeblock.CodeBlocks;
import flaxbeard.automata.common.handler.GuiHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod.EventBusSubscriber
public class CommonProxy
{
	public void preInit()
	{
		CyberwareContent.preInit();
	}

	public void init()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(Automata.INSTANCE, new GuiHandler());
	}

	public void postInit()
	{
		CodeBlocks.preInit();

	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		for (Block block : CyberwareContent.blocks)
		{
			event.getRegistry().register(block);
		}
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		for (Item item : CyberwareContent.items)
		{
			event.getRegistry().register(item);
		}
	}
}
