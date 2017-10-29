package flaxbeard.automata.common.network;

import flaxbeard.automata.common.block.tile.TileEntityProgrammer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateProgrammerPacket implements IMessage
{
    private BlockPos pos;
    public NBTTagCompound data;

    public UpdateProgrammerPacket() {}

    public UpdateProgrammerPacket(TileEntityProgrammer programmer, NBTTagCompound data)
    {
        this.pos = programmer.getPos();
        this.data = data;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {

        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        ByteBufUtils.writeTag(buf, data);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();

        pos = new BlockPos(x, y, z);
        data = ByteBufUtils.readTag(buf);
    }

    public static class Handler implements IMessageHandler<UpdateProgrammerPacket, IMessage>
    {

        @Override
        public IMessage onMessage(UpdateProgrammerPacket message, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;
            DimensionManager.getWorld(player.world.provider.getDimension()).addScheduledTask(new DoSync(player, message.pos, message.data));

            return null;
        }

    }

    private static class DoSync implements Runnable
    {
        private final BlockPos pos;
        private final EntityPlayer player;
        public final NBTTagCompound data;

        public DoSync(EntityPlayer player, BlockPos pos, NBTTagCompound data) {
            this.player = player;
            this.data = data;
            this.pos = pos;
        }


        @Override
        public void run()
        {
            if (player != null) {
                World world = player.getEntityWorld();

                TileEntity te = world.getTileEntity(pos);
                if (te instanceof TileEntityProgrammer) {
                    ((TileEntityProgrammer) te).setData(data);
                }
            }
        }


    }


}