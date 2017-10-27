package flaxbeard.automata.common.block;

import flaxbeard.automata.Automata;
import flaxbeard.automata.common.block.tile.TileEntityProgrammer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockProgrammer extends BlockAutomataBase implements ITileEntityProvider
{
	public BlockProgrammer()
	{
		super(Material.IRON, "testBlock");
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityProgrammer();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		TileEntity te = worldIn.getTileEntity(pos);

		if (te instanceof TileEntityProgrammer)
		{
			playerIn.openGui(Automata.INSTANCE, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}
}
