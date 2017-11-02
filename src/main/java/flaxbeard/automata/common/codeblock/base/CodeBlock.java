package flaxbeard.automata.common.codeblock.base;

import flaxbeard.automata.Automata;
import flaxbeard.automata.client.gui.GuiProgrammer;
import flaxbeard.automata.common.codeblock.CodeBlockRegistry;
import flaxbeard.automata.common.codeblock.component.BlockSlot;
import flaxbeard.automata.common.codeblock.component.Component;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public abstract class CodeBlock {

    private static final ResourceLocation WHITE_PX =
            new ResourceLocation(Automata.MODID + ":textures/gui/whitepx.png");

    protected static final ResourceLocation METAL_TEXTURE =
            new ResourceLocation(Automata.MODID + ":textures/gui/metal2.png");

    protected final Component[] components;
    protected BlockSlot[] slots;
    protected BlockSlot slotIn;

    protected int height;
    protected int width;
    protected int renderHeight;
    protected int renderWidth;

    protected float[] color;

    public CodeBlock(Component... components) {
        this.components = components;

        int slotCount = 0;
        for (Component component : components) {
            component.setBlock(this);
            if (component instanceof BlockSlot) {
                slotCount++;
            }
        }

        slots = new BlockSlot[slotCount];

        int i = 0;
        for (Component component : components) {
            if (component instanceof BlockSlot) {
                slots[i] = (BlockSlot) component;
                i++;
            }
        }

        recalculateComponentPositions();

        color = new float[3];
        setColor("#dbc98e");
    }

    public CodeBlock setColor(String hex) {
        Color c = Color.decode(hex);
        color[0] = c.getRed() / 255.f;
        color[1] = c.getGreen() / 255.f;
        color[2] = c.getBlue() / 255.f;
        return this;
    }

    public void removeFromParent() {
        if (slotIn != null) {
            slotIn.removeContents();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getRenderWidth() {
        return renderWidth;
    }

    public int getRenderHeight() {
        return renderHeight;
    }

    public void drawBackground(GuiProgrammer gui) {

        GlStateManager.pushMatrix();
        drawBlockShape(gui);
        GlStateManager.popMatrix();

        for (Component component : components) {
            drawComponentBackground(gui, component);

        }

        GlStateManager.popMatrix();
    }

    protected void drawBlockShape(GuiProgrammer gui) {
        int width = getRenderWidth();
        int height = getRenderHeight();

        gui.mc.getTextureManager().bindTexture(METAL_TEXTURE);

        GlStateManager.pushMatrix();
        GlStateManager.color(color[0], color[1], color[2]);


        int leftEdgeWidth = drawLeftEdge(gui);
        int widthLeft = width - leftEdgeWidth;
        int loc = leftEdgeWidth;
        int amnt = 81;
        int over = 15;
        while (widthLeft >= amnt) {
            gui.drawTexturedModalRect(loc, 0, over, 0, amnt, height - 2);
            gui.drawTexturedModalRect(loc, height - 2, over, 98, amnt, 2);
            widthLeft -= amnt;
            loc += amnt;
        }
        int rightEdgeWidth = drawRightEdge(gui);
        if (widthLeft > rightEdgeWidth) {
            gui.drawTexturedModalRect(loc, 0, 15, 0, widthLeft - rightEdgeWidth, height - 2);
            gui.drawTexturedModalRect(loc, height - 2, 15, 98, widthLeft - rightEdgeWidth, 2);
        }
    }

    protected int drawLeftEdge(GuiProgrammer gui) {
        gui.drawTexturedModalRect(0, 0, 102, 0, 2, getRenderHeight() - 2);
        gui.drawTexturedModalRect(0, getRenderHeight() - 2, 102, 98, 2, 2);
        return 2;
    }

    protected int drawRightEdge(GuiProgrammer gui) {
        gui.drawTexturedModalRect(getRenderWidth() - 2, 0, 98, 0, 2, getRenderHeight());
        gui.drawTexturedModalRect(getRenderWidth() - 2, getRenderHeight() - 2, 98, 98, 2, 2);
        return 2;
    }

    public void drawForeground(GuiProgrammer gui) {

        GlStateManager.pushMatrix();

        for (Component component : components) {
            drawComponentForeground(gui, component);
        }

        GlStateManager.popMatrix();
    }

    protected void drawComponentForeground(GuiProgrammer gui, Component component) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(component.getStartX(), component.getStartY(), 0);
        component.drawForeground(gui);
        GlStateManager.popMatrix();
    }

    protected void drawComponentBackground(GuiProgrammer gui, Component component) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(component.getStartX(), component.getStartY(), 0);
        component.drawBackground(gui);
        GlStateManager.popMatrix();
    }

    public CodeBlock getHoveredBlock(int x, int y) {
        BlockSlot hoveredSlot = getHoveredSlot(x, y);
        if (hoveredSlot != null && hoveredSlot.getContents() != null) {
            return hoveredSlot.getContents();
        }
        return this;
    }

    public BlockSlot getHoveredSlot(int x, int y) {

        for (BlockSlot slot : slots) {
            BlockSlot result = getHoveredSlotHelper(slot, x, y);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    protected BlockSlot getHoveredSlotHelper(BlockSlot slot, int x, int y) {
        if (slot.isWithinBounds(x, y)) {
            CodeBlock child = slot.getContents();
            if (child != null) {
                BlockSlot lowerSlot = child.getHoveredSlot(x - slot.getStartX(), y - slot.getStartY());
                if (lowerSlot != null) {
                    return lowerSlot;
                }
            }
            return slot;
        }
        return null;
    }



    public void recalculateComponentPositions() {
        renderHeight = 0;
        height = 0;
        for (Component component : components) {
            renderHeight = Math.max(renderHeight, component.getHeight());
        }
        renderHeight += 4;
        height += renderHeight;

        width = getLeftPadding();

        for (Component component : components) {
            int ch = component.getHeight();
            int cw = component.getWidth();

            int py = (renderHeight - ch) / 2;

            component.setStartX(width);
            component.setEndX(width + cw);
            component.setStartY(py);
            component.setEndY(py + ch);

            width += cw;
        }
        width += getRightPadding();
        renderWidth = width;

        if (hasParent()) {
            getParent().recalculateComponentPositions();
        }
    }

    protected int getLeftPadding() {
        return 3;
    }

    protected int getRightPadding() {
        return 3;
    }

    public int getAbsoluteX() {
        if (slotIn == null) {
            return 0;
        }
        return slotIn.getStartX() + getParent().getAbsoluteX();
    }

    public int getAbsoluteY() {
        if (slotIn == null) {
            return 0;
        }
        return slotIn.getStartY() + getParent().getAbsoluteY();
    }

    public boolean hasParent() {
        return slotIn != null;
    }

    public CodeBlock getParent() {
        if (hasParent()) {
            return slotIn.getBlock();
        }
        return null;
    }

    public void setSlotIn(BlockSlot slotIn) {
        this.slotIn = slotIn;
    }

    public final NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = saveData(compound);
        ResourceLocation id = CodeBlockRegistry.getId(this);
        compound.setString(CodeBlockRegistry.ID_STRING, id.toString());
        return compound;
    }

    public final void loadFromNBT(NBTTagCompound compound) {
        loadData(compound);
    }

    protected NBTTagCompound saveData(NBTTagCompound compound) {
        NBTTagList slotTagList = new NBTTagList();
        for (BlockSlot slot : slots) {
            NBTTagCompound slotTag = new NBTTagCompound();
            if (slot.getContents() != null) {
                slotTag = slot.getContents().writeToNBT(slotTag);
            }
            slotTagList.appendTag(slotTag);
        }
        compound.setTag("slots", slotTagList);
        return compound;
    }

    protected void loadData(NBTTagCompound compound) {
        NBTTagList slotTagList = (NBTTagList) compound.getTag("slots");
        for (int i = 0; i < slotTagList.tagCount(); i++) {
            NBTTagCompound slotTag = slotTagList.getCompoundTagAt(i);
            CodeBlock block = CodeBlockRegistry.loadFromNBT(slotTag);
            slots[i].setContents(block);
        }
    }

}
