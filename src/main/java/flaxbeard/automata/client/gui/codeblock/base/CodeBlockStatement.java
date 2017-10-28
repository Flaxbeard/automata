package flaxbeard.automata.client.gui.codeblock.base;

import flaxbeard.automata.client.gui.GuiProgrammer;
import flaxbeard.automata.client.gui.codeblock.component.BlockSlot;
import flaxbeard.automata.client.gui.codeblock.component.Component;
import flaxbeard.automata.client.gui.codeblock.component.FollowingSlot;

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
