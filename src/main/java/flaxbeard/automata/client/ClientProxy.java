package flaxbeard.automata.client;

import flaxbeard.automata.client.renderer.RenderAutomaton;
import flaxbeard.automata.common.CommonProxy;
import flaxbeard.automata.common.entity.EntityAutomaton;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityAutomaton.class, RenderAutomaton::new);
		super.preInit();
	}
}
