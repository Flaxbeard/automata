package flaxbeard.automata.common.item;

import flaxbeard.automata.common.block.tile.TileEntityProgrammer;
import flaxbeard.automata.common.codeblock.CodeBlockHead;
import flaxbeard.automata.common.codeblock.CodeBlockRegistry;
import flaxbeard.automata.common.entity.EntityAutomaton;
import flaxbeard.automata.common.program.base.Statement;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemGizmo extends ItemAutomataBase {

    public ItemGizmo() {
        super("gizmo", 1);
    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityProgrammer) {
            NBTTagCompound data = ((TileEntityProgrammer) te).getData();

            if (data == null || !data.hasKey("head")) {
                return EnumActionResult.FAIL;
            }

            NBTTagCompound headBlockTag = data.getCompoundTag("head");
            CodeBlockHead head = (CodeBlockHead) CodeBlockRegistry.loadFromNBT(headBlockTag);

            if (head == null) {
                return EnumActionResult.FAIL;
            }

            Statement program = head.toStatement();
            NBTTagCompound programCompound = program.writeToNBT(new NBTTagCompound());

            setData(itemstack, programCompound);
            System.out.println("DATA GET");
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }

    public void setData(ItemStack itemstack, NBTTagCompound data) {
        if (!itemstack.hasTagCompound()) {
            itemstack.setTagCompound(new NBTTagCompound());
        }
        itemstack.getTagCompound().setTag("programData", data);
    }

    public NBTTagCompound getData(ItemStack itemstack) {
        if (itemstack.hasTagCompound()) {
            NBTTagCompound compound = itemstack.getTagCompound();
            if (compound.hasKey("programData")) {
                return compound.getCompoundTag("programData");
            }
        }
        return null;
    }

    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand)
    {
        if (target instanceof EntityAutomaton && getData(stack) != null) {
            ((EntityAutomaton) target).setProgramItem(stack);
            System.out.println("DATA SET");
            return true;
        }
        return false;
    }
}
