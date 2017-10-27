package flaxbeard.automata.common.block.tile;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityCyberwareBase extends TileEntity
{
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
	{
		if (oldState.getBlock() != newState.getBlock())
		{
			return true;
		}
		return oldState.getBlock().getMetaFromState(oldState) != newState.getBlock().getMetaFromState(newState);
	}
	
	public void markBlockForUpdate()
	{
		markBlockForUpdate(null);
	}
	
	public void markBlockForUpdate(@Nullable IBlockState newState)
	{
		IBlockState currentState = world.getBlockState(getPos());
		if (newState == null) newState = currentState;
		
		world.notifyBlockUpdate(pos, currentState, newState, 3);
		if (newState != currentState) world.notifyNeighborsOfStateChange(pos, newState.getBlock(), true);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.readNBT(nbt, false);
	}
	
	public void readNBT(NBTTagCompound nbt, boolean descPacket) {};
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		this.writeNBT(nbt, false);
		return nbt;
	}
	
	public void writeNBT(NBTTagCompound nbt, boolean descPacket) {};

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		this.writeNBT(nbttagcompound, true);
		return new SPacketUpdateTileEntity(this.pos, 3, nbttagcompound);
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound nbt = super.writeToNBT(new NBTTagCompound());
		writeNBT(nbt, true);
		return nbt;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		this.readNBT(pkt.getNbtCompound(), true);
	}
}
