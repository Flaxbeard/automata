package flaxbeard.automata.common.network;

import flaxbeard.automata.Automata;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Automata.MODID);

    public static void preInit()
    {
        INSTANCE.registerMessage(UpdateProgrammerPacket.Handler.class, UpdateProgrammerPacket.class, 0, Side.SERVER);
    }
}