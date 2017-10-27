package flaxbeard.automata.common.handler;

import flaxbeard.automata.client.gui.ContainerProgrammer;
import flaxbeard.automata.client.gui.GuiProgrammer;
import flaxbeard.automata.common.block.tile.TileEntityProgrammer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {

	@Nullable
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new ContainerProgrammer(player.inventory, (TileEntityProgrammer) world.getTileEntity(new BlockPos(x, y, z)));
	}

	@Nullable
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return new GuiProgrammer(player.inventory, (TileEntityProgrammer) world.getTileEntity(new BlockPos(x, y, z)));
	}
}
