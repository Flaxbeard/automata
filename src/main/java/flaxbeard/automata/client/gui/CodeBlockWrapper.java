package flaxbeard.automata.client.gui;

import flaxbeard.automata.common.codeblock.CodeBlockRegistry;
import flaxbeard.automata.common.codeblock.base.CodeBlock;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;

public class CodeBlockWrapper {

    private CodeBlock block;

    private int x;
    private int y;

    public CodeBlockWrapper(CodeBlock block, int x, int y) {
        this.block = block;
        this.x = x;
        this.y = y;
    }

    public void drawForeground(GuiProgrammer gui) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(GuiProgrammer.BLOCK_SCALE, GuiProgrammer.BLOCK_SCALE, 0);
        block.drawForeground(gui);
        GlStateManager.popMatrix();
    }

    public void drawBackground(GuiProgrammer gui) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(GuiProgrammer.BLOCK_SCALE, GuiProgrammer.BLOCK_SCALE, 0);
        block.drawBackground(gui);
        GlStateManager.popMatrix();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public CodeBlockWrapper setPos(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public int getHeight() {
        return (int) (block.getHeight() * GuiProgrammer.BLOCK_SCALE);
    }

    public int getWidth() {
        return (int) (block.getWidth() * GuiProgrammer.BLOCK_SCALE);
    }

    public CodeBlock getBlock() {
        return block;
    }

    public void readFromNBT(NBTTagCompound compound) {
        x = compound.getInteger("x");
        y = compound.getInteger("y");
        block = CodeBlockRegistry.loadFromNBT(compound.getCompoundTag("block"));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("x", x);
        compound.setInteger("y", y);
        compound.setTag("block", block.writeToNBT(new NBTTagCompound()));
        return compound;
    }
}