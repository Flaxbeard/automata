package flaxbeard.automata.client.gui;

import flaxbeard.automata.common.codeblock.CodeBlockRegistry;
import flaxbeard.automata.common.codeblock.base.CodeBlock;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;

public class CodeBlockTemplate {

    private CodeBlock block;

    private int x;
    private int y;

    public CodeBlockTemplate(CodeBlock block, int x, int y) {
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

    public CodeBlockTemplate setPos(int x, int y) {
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

    public CodeBlockWrapper getCopy() {
        CodeBlock newBlock = CodeBlockRegistry.createNew(block);
        return new CodeBlockWrapper(newBlock, x, y);
    }

}