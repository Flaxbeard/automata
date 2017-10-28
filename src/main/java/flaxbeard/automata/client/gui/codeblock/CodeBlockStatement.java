package flaxbeard.automata.client.gui.codeblock;

import flaxbeard.automata.client.gui.GuiProgrammer;
import flaxbeard.automata.client.gui.codeblock.component.BlockSlot;
import flaxbeard.automata.client.gui.codeblock.component.Component;
import flaxbeard.automata.client.gui.codeblock.component.FollowingSlot;
import net.minecraft.client.renderer.GlStateManager;

public abstract class CodeBlockStatement extends CodeBlock {

    private FollowingSlot followingSlot;

    public CodeBlockStatement(Component... components) {
        super(components);

        followingSlot = new FollowingSlot();
        followingSlot.setBlock(this);
        recalculateComponentPositions();
    }

    @Override
    public BlockSlot getHoveredSlot(int x, int y) {
        if (followingSlot.isWithinBounds(x, y)) {
            BlockSlot result = getHoveredSlotHelper(followingSlot, x, y);
            if (result != null) {
                return result;
            }
        }

        return super.getHoveredSlot(x, y);
    }

    @Override
    public void recalculateComponentPositions() {
        super.recalculateComponentPositions();

        int y = renderHeight - 1;
        if (followingSlot != null) {
            followingSlot.setStartY(y);
            followingSlot.setEndY(y + followingSlot.getHeight());
            height += followingSlot.getHeight();
            followingSlot.setStartX(0);
            followingSlot.setEndX(followingSlot.getWidth());

            width = Math.max(width, followingSlot.getWidth());
        }

        if (hasParent()) {
            getParent().recalculateComponentPositions();
        }
    }

    @Override
    public void drawBackground(GuiProgrammer gui) {
        super.drawBackground(gui);

        drawComponentBackground(gui, followingSlot);
    }

    @Override
    public void drawForeground(GuiProgrammer gui) {
        super.drawForeground(gui);

        drawComponentForeground(gui, followingSlot);
    }

    @Override
    protected int getLeftPadding() {
        return 12;
    }

    /*@Override
    protected void drawBlockShape(GuiProgrammer gui) {
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
    }*/

    @Override
    protected int drawLeftEdge(GuiProgrammer gui) {
        gui.drawTexturedModalRect(0, 0, 0, 0, 15, getRenderHeight() - 2);
        gui.drawTexturedModalRect(0, getRenderHeight() - 2, 0, 98, 15, 2);
        gui.drawTexturedModalRect(0, getRenderHeight(), 0, 100, 20, 5);
        return 15;
    }

    public FollowingSlot getFollowingSlot() {
        return followingSlot;
    }
}
