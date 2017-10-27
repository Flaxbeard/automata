package flaxbeard.automata.common.block.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockAutomata extends ItemBlock
{
	private String[] tt;
	public int[] metaMap = new int[] { 0 };
	
	public ItemBlockAutomata(Block block)
	{
		super(block);
	}
	
	public ItemBlockAutomata(Block block, String... tooltip)
	{
		super(block);
		this.tt = tooltip;
	}

	public ItemBlockAutomata(Block block, int[] metaMap)
	{
		super(block);
		this.metaMap = metaMap;
	}

	/*@Override
	public EnumCategory getCategory(ItemStack stack)
	{
		return EnumCategory.BLOCKS;
	}*/

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		if (this.tt != null)
		{
			for (String str : tt)
			{
				tooltip.add(ChatFormatting.DARK_GRAY + I18n.format(str));
			}
		}
	}
	
	@Override
	public int getMetadata(int damage)
	{
		return metaMap[Math.min(metaMap.length - 1, damage)];
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		for (int i = 0; i < metaMap.length; i++)
		{
			subItems.add(new ItemStack(this, 1, metaMap[i]));
		}
	}
	
	@Override
	public String getUnlocalizedName(@Nonnull ItemStack stack)
	{
		if (metaMap.length == 1 && metaMap[0] == 0)
		{
			return super.getUnlocalizedName(stack);
		}
		
		return super.getUnlocalizedName(stack) + "." + stack.getItemDamage();
	}
}
