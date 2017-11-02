package flaxbeard.automata.common;

import flaxbeard.automata.Automata;
import flaxbeard.automata.common.handler.GuiHandler;
import flaxbeard.automata.common.network.PacketHandler;
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
		AutomataContent.preInit();
		PacketHandler.preInit();
	}

	public void init()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(Automata.INSTANCE, new GuiHandler());
	}

	public void postInit()
	{

	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		for (Block block : AutomataContent.blocks)
		{
			event.getRegistry().register(block);
		}
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		for (Item item : AutomataContent.items)
		{
			event.getRegistry().register(item);
		}
	}
}
