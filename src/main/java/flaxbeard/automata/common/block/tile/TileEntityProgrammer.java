package flaxbeard.automata.common.block.tile;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityProgrammer extends TileEntityCyberwareBase
{

    private NBTTagCompound data;

    public void setData(NBTTagCompound data) {
        this.data = data;
        markDirty();
    }

    public NBTTagCompound getData() {
        return data;
    }

    @Override
    public void writeNBT(NBTTagCompound nbt, boolean descPacket) {
        super.writeNBT(nbt, descPacket);
        nbt.setTag("progData", data);
    }

    @Override
    public void readNBT(NBTTagCompound nbt, boolean descPacket) {
        super.readNBT(nbt, descPacket);
        data = nbt.getCompoundTag("progData");
    }
}
