package flaxbeard.automata.client.gui.codeblock;

import flaxbeard.automata.client.gui.GuiProgrammer;
import flaxbeard.automata.client.gui.codeblock.component.BlockSlot;
import flaxbeard.automata.client.gui.codeblock.component.Component;
import flaxbeard.automata.client.gui.codeblock.component.FollowingSlot;

public abstract class CodeBlockStatement extends CodeBlock {

    private static final String FOLLOWING_SLOT_KEY = "followingSlot";

    public CodeBlockStatement(Component... components) {
        super(components);

    }

    @Override
    public Component[] getInstanceComponents() {
        return super.getInstanceComponents();
    }

    @Override
    public void setup(CodeBlockInstance codeBlockInstance) {
        FollowingSlot followingSlot = new FollowingSlot();
        followingSlot.setBlock(codeBlockInstance);

        codeBlockInstance.setData(FOLLOWING_SLOT_KEY,followingSlot);
    }

    public FollowingSlot getFollowingSlot(CodeBlockInstance codeBlockInstance) {
        return (FollowingSlot) codeBlockInstance.getData(FOLLOWING_SLOT_KEY);
    }

    @Override
    public BlockSlot getHoveredSlot(CodeBlockInstance codeBlockInstance, int x, int y) {
        FollowingSlot followingSlot = getFollowingSlot(codeBlockInstance);

        if (followingSlot.isWithinBounds(x, y)) {
            BlockSlot result = getHoveredSlotHelper(followingSlot, x, y);
            if (result != null) {
                return result;
            }
        }

        return super.getHoveredSlot(codeBlockInstance, x, y);
    }

    @Override
    public void recalculateComponentPositions(CodeBlockInstance codeBlockInstance) {
        super.recalculateComponentPositions(codeBlockInstance);

        FollowingSlot followingSlot = getFollowingSlot(codeBlockInstance);

        int y = codeBlockInstance.getRenderHeight() - 1;
        if (followingSlot != null) {
            followingSlot.setStartY(y);
            followingSlot.setEndY(y + followingSlot.getHeight());
            codeBlockInstance.setHeight(codeBlockInstance.getHeight() + followingSlot.getHeight());
            followingSlot.setStartX(0);
            followingSlot.setEndX(followingSlot.getWidth());

            codeBlockInstance.setWidth(Math.max(codeBlockInstance.getWidth(), followingSlot.getWidth()));
        }

        if (codeBlockInstance.hasParent()) {
            codeBlockInstance.getParent().recalculateComponentPositions();
        }
    }

    @Override
    public void drawBackground(CodeBlockInstance codeBlockInstance, GuiProgrammer gui) {
        super.drawBackground(codeBlockInstance, gui);

        drawComponentBackground(gui, getFollowingSlot(codeBlockInstance));
    }

    @Override
    public void drawForeground(CodeBlockInstance codeBlockInstance, GuiProgrammer gui) {
        super.drawForeground(codeBlockInstance, gui);

        drawComponentForeground(gui, getFollowingSlot(codeBlockInstance));
    }

    @Override
    protected int getLeftPadding(CodeBlockInstance codeBlockInstance) {
        return 12;
    }


    @Override
    protected int drawLeftEdge(CodeBlockInstance codeBlockInstance, GuiProgrammer gui) {
        int renderHeight = codeBlockInstance.getRenderHeight();

        gui.drawTexturedModalRect(0, 0, 0, 0, 15, renderHeight - 2);
        gui.drawTexturedModalRect(0, renderHeight - 2, 0, 98, 15, 2);
        gui.drawTexturedModalRect(0, renderHeight, 0, 100, 20, 5);
        return 15;
    }
}
