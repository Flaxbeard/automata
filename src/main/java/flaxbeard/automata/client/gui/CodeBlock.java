package flaxbeard.automata.client.gui;

import flaxbeard.automata.Automata;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public abstract class CodeBlock {

    private static final ResourceLocation WHITE_PX =
            new ResourceLocation(Automata.MODID + ":textures/gui/whitepx.png");

    private static final ResourceLocation METAL_TEXTURE =
            new ResourceLocation(Automata.MODID + ":textures/gui/metal2.png");

    private final Component[] components;
    public CodeBlock[] children;
    private BlockSlot slotIn;

    private int[] componentX;
    private int[] componentY;
    private int[] componentX2;
    private int[] componentY2;

    private int height;
    private int width;
    private int renderHeight;
    private int renderWidth;

    public boolean hasBottomConnection;

    public CodeBlock(Component... components) {
        this.components = components;

        hasBottomConnection = false;
        for (Component component : components) {
            if (component instanceof FollowingComponent) {
                hasBottomConnection = true;
                break;
            }
        }

        this.children = new CodeBlock[components.length];

        this.componentX = new int[components.length];
        this.componentX2 = new int[components.length];
        this.componentY = new int[components.length];
        this.componentY2 = new int[components.length];

        recalculateComponentPositions();
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
        int width = getRenderWidth();
        int height = getRenderHeight();

        gui.mc.getTextureManager().bindTexture(METAL_TEXTURE);

        GlStateManager.pushMatrix();
        GlStateManager.color(.86f, .791f, .559f);

        int widthLeft = width - 15;

        gui.drawTexturedModalRect(0, 0, 0, 0, 15, height - 2);
        gui.drawTexturedModalRect(0, height - 2, 0, 98, 15, 2);

        int loc = 15;
        int amnt = 96 - 15;
        int over = 15;
        while (widthLeft >= amnt) {
            gui.drawTexturedModalRect(loc, 0, over, 0, amnt, height - 2);
            gui.drawTexturedModalRect(loc, height - 2, over, 98, amnt, 2);
            widthLeft -= amnt;
            loc += amnt;
        }
        if (widthLeft > 2) {
            gui.drawTexturedModalRect(loc, 0, 15, 0, widthLeft - 2, height - 2);
            gui.drawTexturedModalRect(loc, height - 2, 15, 98, widthLeft - 2, 2);
        }

        gui.drawTexturedModalRect(width - 2, 0, 98, 0, 2, height);
        gui.drawTexturedModalRect(width - 2, height - 2, 98, 98, 2, 2);

        gui.drawTexturedModalRect(0, height, 0, 100, 20, 5);


        GlStateManager.popMatrix();



        for (int i = 0; i < components.length; i++) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(componentX[i], componentY[i], 0);
            components[i].drawBackground(gui, this, children[i]);
            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();
    }

    public void drawForeground(GuiProgrammer gui) {

        GlStateManager.pushMatrix();
        int height = getHeight();

        for (int i = 0; i < components.length; i++) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(componentX[i], componentY[i], 0);
            components[i].drawForeground(gui, this, children[i]);
            GlStateManager.popMatrix();
        }

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
        for (int i = 0; i < components.length; i++) {

            if (components[i] instanceof SlotComponent) {
                if (x >= componentX[i] && x <= componentX2[i]
                        && y >= componentY[i] && y <= componentY2[i]) {
                    if (children[i] != null) {
                        BlockSlot downCall = children[i].getHoveredSlot(x - componentX[i], y - componentY[i]);
                        if (downCall != null) {
                            return downCall;
                        }
                    }
                    return new BlockSlot(this, i);
                }
            }
        }

        return null;
    }



    protected void recalculateComponentPositions() {
        renderHeight = 0;
        height = 0;
        for (int i = 0; i < components.length; i++) {
            if (!(components[i] instanceof FollowingComponent)) {
                renderHeight = Math.max(height, components[i].getHeight(this, children[i]));
            } else {
                height += components[i].getHeight(this, children[i]);
            }
        }
        renderHeight += 6;
        height += renderHeight;

        width = 12;

        for (int i = 0; i < components.length; i++) {
            if (!(components[i] instanceof FollowingComponent)) {

                int ch = components[i].getHeight(this, children[i]);
                int cw = components[i].getWidth(this, children[i]);

                int py = (renderHeight - ch) / 2;

                componentX[i] = width;
                componentX2[i] = width + cw;
                componentY[i] = py;
                componentY2[i] = py + ch;

                width += cw;
            } else {
                int ch = components[i].getHeight(this, children[i]);
                int cw = components[i].getWidth(this, children[i]);

                componentX[i] = 0;
                componentX2[i] = cw;
                componentY[i] = renderHeight - 3;
                componentY2[i] = renderHeight + ch;
            }
        }
        renderWidth = width;

        if (hasParent()) {
            slotIn.getParent().recalculateComponentPositions();
        }
    }

    public int getAbsoluteX() {
        if (slotIn == null) {
            return 0;
        }
        return slotIn.getStartX() + slotIn.getParent().getAbsoluteX();
    }

    public int getAbsoluteY() {
        if (slotIn == null) {
            return 0;
        }
        return slotIn.getStartY() + slotIn.getParent().getAbsoluteY();
    }

    public boolean hasParent() {
        return slotIn != null;
    }

    public static abstract class Component {

        public abstract int getWidth(CodeBlock block, CodeBlock child);
        public abstract int getHeight(CodeBlock block, CodeBlock child);
        public void drawForeground(GuiProgrammer gui, CodeBlock block, CodeBlock child) {};
        public void drawBackground(GuiProgrammer gui, CodeBlock block, CodeBlock child) {};

    }

    public static class StringComponent extends Component {
        private static FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        private String text;

        public StringComponent(String text) {
            this.text = text;
        }

        @Override
        public int getWidth(CodeBlock block, CodeBlock child) {
            return font.getStringWidth(text);
        }

        @Override
        public int getHeight(CodeBlock block, CodeBlock child) {
            return font.FONT_HEIGHT;
        }

        @Override
        public void drawForeground(GuiProgrammer gui, CodeBlock block, CodeBlock child) {
            font.drawString(text, 0, 0, 0);
        }
    }

    public static class SlotComponent extends Component {

        @Override
        public int getWidth(CodeBlock block, CodeBlock child) {
            if (child == null) {
                return 11;
            }
            return child.getWidth();
        }

        @Override
        public int getHeight(CodeBlock block, CodeBlock child) {
            if (child == null) {
                return 9;
            }
            return child.getHeight();
        }

        @Override
        public void drawBackground(GuiProgrammer gui, CodeBlock block, CodeBlock child) {
            if (child == null) {
                GlStateManager.color(0, 0, 0, 0.5f);
                gui.mc.getTextureManager().bindTexture(WHITE_PX);
                gui.drawTexturedModalRect(2, 0, 0, 0, 7, 9);
                GlStateManager.color(1, 1, 1, 1);
            } else {
                child.drawBackground(gui);
            }
        }

        @Override
        public void drawForeground(GuiProgrammer gui, CodeBlock block, CodeBlock child) {
            if (child != null) {
                child.drawForeground(gui);
            }
        }
    }

    public static class FollowingComponent extends SlotComponent {
        @Override
        public int getWidth(CodeBlock block, CodeBlock child) {
            if (child == null) {
                return 50;
            }
            return child.getWidth();
        }

        @Override
        public int getHeight(CodeBlock block, CodeBlock child) {
            if (child == null) {
                return 5;
            }
            return child.getHeight();
        }

        @Override
        public void drawBackground(GuiProgrammer gui, CodeBlock block, CodeBlock child) {
            if (child != null) {
                child.drawBackground(gui);
            }
        }

    }

    public static class BlockSlot {

        private final CodeBlock block;
        private final int position;

        public BlockSlot(CodeBlock block, int position) {
            this.block = block;
            this.position = position;
        }

        public CodeBlock getParent() {
            return block;
        }
        
        public int getStartX() {
            return block.componentX[position];
        }

        public int getEndX() {
            return block.componentX2[position];
        }

        public int getStartY() {
            return block.componentY[position];
        }

        public int getEndY() {
            return block.componentY2[position];
        }

        public CodeBlock getContents() {
            return block.children[position];
        }

        public void removeContents() {
            getContents().slotIn = null;
            block.children[position] = null;
            block.recalculateComponentPositions();
        }

        public boolean setContents(CodeBlock newContents) {
            if (newContents == block || (newContents != null && newContents.hasParent())) {
                return false;
            }

            if (getContents() != null) {
                return false;
            }

            if (newContents != null) {
                newContents.slotIn = this;
            }

            block.children[position] = newContents;
            block.recalculateComponentPositions();

            return true;
        }
    }
}
