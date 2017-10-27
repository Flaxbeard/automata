package flaxbeard.automata;

import flaxbeard.automata.common.CommonProxy;
import flaxbeard.automata.common.misc.TabCyberware;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Automata.MODID, version = Automata.VERSION)
public class Automata
{
	public static final String MODID = "automata";
	public static final String VERSION = "@VERSION@";
	
	@Instance(MODID)
	public static Automata INSTANCE;
	
	@SidedProxy(clientSide = "flaxbeard.automata.client.ClientProxy", serverSide = "flaxbeard.automata.common.CommonProxy")
	public static CommonProxy proxy;
		
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init();
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit();
	}
	
	public static CreativeTabs creativeTab = new TabCyberware(MODID);

}
