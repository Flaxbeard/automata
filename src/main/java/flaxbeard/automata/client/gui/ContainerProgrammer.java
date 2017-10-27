package flaxbeard.automata.client.gui;

import flaxbeard.automata.common.block.tile.TileEntityProgrammer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerProgrammer extends Container {

    private final TileEntityProgrammer programmer;

    public ContainerProgrammer(InventoryPlayer inventoryPlayer, TileEntityProgrammer programmer) {
        this.programmer = programmer;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
