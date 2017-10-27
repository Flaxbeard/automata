package flaxbeard.automata.common.block;

import javax.annotation.Nullable;

import flaxbeard.automata.Automata;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import flaxbeard.automata.common.CyberwareContent;
import flaxbeard.automata.common.block.item.ItemBlockAutomata;

public class BlockAutomataBase extends Block
{

	public BlockAutomataBase(Material materialIn, String name, boolean customIb)
	{
		this(materialIn, materialIn.getMaterialMapColor(), name, customIb);
	}

	public BlockAutomataBase(Material materialIn, String name)
	{
		this(materialIn, materialIn.getMaterialMapColor(), name);
	}

	public BlockAutomataBase(Material materialIn, MapColor mapColorIn, String name)
	{
		this(materialIn, mapColorIn, name, false);
	}

	public BlockAutomataBase(Material materialIn, MapColor mapColorIn, String name, boolean customIb)
	{
		super(materialIn, mapColorIn);

		this.setRegistryName(name);

		if (!customIb)
		{
			ItemBlock ib = new ItemBlockAutomata(this);
			ib.setRegistryName(name);
			CyberwareContent.items.add(ib);
		}

		this.setUnlocalizedName(Automata.MODID + "." + name);

		this.setCreativeTab(Automata.creativeTab);

		this.isBlockContainer = this instanceof ITileEntityProvider;

		if (this instanceof ITileEntityProvider)
		{
			GameRegistry.registerTileEntity(((ITileEntityProvider) this).createNewTileEntity(null, 0).getClass(), Automata.MODID + ":" + name);
		}

		CyberwareContent.blocks.add(this);
	}

	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		super.breakBlock(worldIn, pos, state);
		if (this instanceof ITileEntityProvider)
		{
			worldIn.removeTileEntity(pos);
		}
	}

	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
	{
		if (this instanceof ITileEntityProvider)
		{
			if (te instanceof IWorldNameable && ((IWorldNameable)te).hasCustomName())
			{
				player.addStat(StatList.getBlockStats(this));
				player.addExhaustion(0.005F);

				if (worldIn.isRemote)
				{
					return;
				}

				int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
				Item item = this.getItemDropped(state, worldIn.rand, i);

				if (item == Items.AIR)
				{
					return;
				}

				ItemStack itemstack = new ItemStack(item, this.quantityDropped(worldIn.rand));
				itemstack.setStackDisplayName(((IWorldNameable)te).getName());
				spawnAsEntity(worldIn, pos, itemstack);
			}
			else
			{
				super.harvestBlock(worldIn, player, pos, state, (TileEntity)null, stack);
			}
		}
	}

	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
	{
		boolean b = super.eventReceived(state, worldIn, pos, id, param);
		if (this instanceof ITileEntityProvider)
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);
			return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
		}
		return b;
	}

}
