package flaxbeard.automata.client.gui.codeblock;


import flaxbeard.automata.Automata;
import flaxbeard.automata.client.gui.GuiProgrammer;
import flaxbeard.automata.client.gui.codeblock.component.BlockSlot;
import flaxbeard.automata.client.gui.codeblock.component.Component;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class CodeBlock {

    protected static final ResourceLocation METAL_TEXTURE =
            new ResourceLocation(Automata.MODID + ":textures/gui/metal2.png");

    private Component[] components;

    public CodeBlock(Component... components) {
        this.components = components;
        CodeBlocks.codeBlockList.add(this);
    }

    public Component[] getInstanceComponents() {
        Component[] newComponents = new Component[components.length];
        for (int i = 0; i < components.length; i++) {
            newComponents[i] = components[i].clone();
        }
        return newComponents;
    }

    public void drawBackground(CodeBlockInstance codeBlockInstance, GuiProgrammer gui) {
        GlStateManager.pushMatrix();
        drawBlockShape(codeBlockInstance, gui);

        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();

        for (Component component : codeBlockInstance.getComponents()) {
            drawComponentBackground(gui, component);
        }

        GlStateManager.popMatrix();
    }


    protected void drawComponentBackground(GuiProgrammer gui, Component component) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(component.getStartX(), component.getStartY(), 0);
        component.drawBackground(gui);
        GlStateManager.popMatrix();
    }

    public void drawForeground(CodeBlockInstance codeBlockInstance, GuiProgrammer gui) {
        GlStateManager.pushMatrix();

        for (Component component : codeBlockInstance.getComponents()) {
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

    protected void drawBlockShape(CodeBlockInstance codeBlockInstance, GuiProgrammer gui) {
        int width = codeBlockInstance.getRenderWidth();
        int height = codeBlockInstance.getRenderHeight();

        gui.mc.getTextureManager().bindTexture(METAL_TEXTURE);

        GlStateManager.pushMatrix();
        
        float[] color = getColor(codeBlockInstance);
        GlStateManager.color(color[0], color[1], color[2]);


        int leftEdgeWidth = drawLeftEdge(codeBlockInstance, gui);
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
        int rightEdgeWidth = drawRightEdge(codeBlockInstance, gui);
        if (widthLeft > rightEdgeWidth) {
            gui.drawTexturedModalRect(loc, 0, 15, 0, widthLeft - rightEdgeWidth, height - 2);
            gui.drawTexturedModalRect(loc, height - 2, 15, 98, widthLeft - rightEdgeWidth, 2);
        }

        GlStateManager.popMatrix();
    }

    protected int drawRightEdge(CodeBlockInstance codeBlockInstance, GuiProgrammer gui) {
        /*gui.drawTexturedModalRect(getRenderWidth() - 2, 0, 98, 0, 2, getRenderHeight());
        gui.drawTexturedModalRect(getRenderWidth() - 2, getRenderHeight() - 2, 98, 98, 2, 2);
        return 2;*/
        for (int i = 1; i < codeBlockInstance.getRenderHeight() - 1; i++) {
            float percentage = i / (codeBlockInstance.getRenderHeight() - 1.0f);
            int type = (int) (percentage * 10);

            if (type < 5) {
                gui.drawTexturedModalRect(codeBlockInstance.getRenderWidth() - 5, i, 1, type, 4, 1);
            } else {
                gui.drawTexturedModalRect(codeBlockInstance.getRenderWidth() - 5, i, 6, (type - 5) + 99, 4, 1);
            }
        }

        return 5;
    }

    protected int drawLeftEdge(CodeBlockInstance codeBlockInstance, GuiProgrammer gui) {
        /*gui.drawTexturedModalRect(0, 0, 102, 0, 2, getRenderHeight() - 2);
        gui.drawTexturedModalRect(0, getRenderHeight() - 2, 102, 98, 2, 2);
        return 2;*/

        for (int i = 1; i < codeBlockInstance.getRenderHeight() - 1; i++) {
            float percentage = i / (codeBlockInstance.getRenderHeight() - 1.0f);
            int type = (int) (percentage * 10);

            if (type < 5) {
                gui.drawTexturedModalRect(0, i, 5, type, 5, 1);
            } else {
                gui.drawTexturedModalRect(0, i, 0, (type - 5) + 99, 5, 1);
            }
        }

        return 5;
    }

    private float[] getColor(CodeBlockInstance codeBlockInstance) {
        float[] color = new float[3];
        Color c = Color.decode("#dbc98e");
        color[0] = c.getRed() / 255.f;
        color[1] = c.getGreen() / 255.f;
        color[2] = c.getBlue() / 255.f;
        return color;
    }

    public CodeBlockInstance getHoveredBlock(CodeBlockInstance codeBlockInstance, int x, int y) {
        BlockSlot hoveredSlot = getHoveredSlot(codeBlockInstance, x, y);
        if (hoveredSlot != null && hoveredSlot.getContents() != null) {
            return hoveredSlot.getContents();
        }
        return codeBlockInstance;
    }

    public BlockSlot getHoveredSlot(CodeBlockInstance codeBlockInstance, int x, int y) {

        for (BlockSlot slot : codeBlockInstance.getSlots()) {
            BlockSlot result = getHoveredSlotHelper(slot, x, y);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    protected BlockSlot getHoveredSlotHelper(BlockSlot slot, int x, int y) {
        if (slot.isWithinBounds(x, y)) {
            CodeBlockInstance child = slot.getContents();
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

    public void recalculateComponentPositions(CodeBlockInstance codeBlockInstance) {
        int renderHeight = 0;
        int height = 0;
        for (Component component : codeBlockInstance.getComponents()) {
            renderHeight = Math.max(renderHeight, component.getHeight());
        }
        renderHeight += 4;
        height += renderHeight;

        codeBlockInstance.setHeight(height);
        codeBlockInstance.setRenderHeight(renderHeight);

        int width = getLeftPadding(codeBlockInstance);

        for (Component component : codeBlockInstance.getComponents()) {
            int ch = component.getHeight();
            int cw = component.getWidth();

            int py = (renderHeight - ch) / 2;

            component.setStartX(width);
            component.setEndX(width + cw);
            component.setStartY(py);
            component.setEndY(py + ch);

            width += cw;
        }
        width += getRightPadding(codeBlockInstance);

        codeBlockInstance.setWidth(width);
        codeBlockInstance.setRenderWidth(width);

        if (codeBlockInstance.hasParent()) {
            codeBlockInstance.getParent().recalculateComponentPositions();
        }
    }

    protected int getLeftPadding(CodeBlockInstance codeBlockInstance) {
        return 3;
    }

    protected int getRightPadding(CodeBlockInstance codeBlockInstance) {
        return 3;
    }

    public void setup(CodeBlockInstance codeBlockInstance) {}
}
