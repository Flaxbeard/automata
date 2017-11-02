package flaxbeard.automata.common.codeblock;

import flaxbeard.automata.Automata;
import flaxbeard.automata.client.Colors;
import flaxbeard.automata.client.gui.GuiProgrammer;
import flaxbeard.automata.common.codeblock.base.CodeBlockStatement;
import flaxbeard.automata.common.codeblock.component.FollowingSlot;
import flaxbeard.automata.common.codeblock.component.StringComponent;
import flaxbeard.automata.common.program.base.Statement;
import flaxbeard.automata.common.program.StmtHead;
import net.minecraft.util.ResourceLocation;

public class CodeBlockHead extends CodeBlockStatement {

    public CodeBlockHead() {
        super(
                new StringComponent("Begin Program")
        );
        setColor(Colors.IRON);
    }


    @Override
    public boolean hasTopConnection() {
        return false;
    }

    @Override
    protected int drawLeftEdge(GuiProgrammer gui) {
        gui.drawTexturedModalRect(0, 0, 102, 0, 2, getRenderHeight() - 2);
        gui.drawTexturedModalRect(2, 0, 15, 0, 23, getRenderHeight() - 2);
        gui.drawTexturedModalRect(10, getRenderHeight() - 2, 0, 98, 15, 2);
        gui.drawTexturedModalRect(10, getRenderHeight(), 0, 100, 20, 5);

        int heightLeft = Math.max(30, getHeight() - getRenderHeight());
        int yPos = getRenderHeight() - 2;
        while (heightLeft > 96) {
            gui.drawTexturedModalRect(0, yPos, 102, 2, 2, 96);
            gui.drawTexturedModalRect(8, yPos, 98, 2, 2, 96);
            gui.drawTexturedModalRect(2, yPos, 15, 2, 6, 96);

            heightLeft -= 96;
            yPos += 96;
        }

        gui.drawTexturedModalRect(0, yPos, 102, 2, 2, heightLeft);
        gui.drawTexturedModalRect(8, yPos, 98, 2, 2, heightLeft);
        gui.drawTexturedModalRect(2, yPos, 15, 2, 6, heightLeft);
        yPos += heightLeft;

        gui.drawTexturedModalRect(0, yPos, 102, 2, 2, 8);
        gui.drawTexturedModalRect(8, yPos, 98, 2, 2, 2);
        gui.drawTexturedModalRect(2, yPos, 15, 2, 6, 8);
        gui.drawTexturedModalRect(9, yPos, 15, 0, 31, 2);
        gui.drawTexturedModalRect(38, yPos + 1, 98, 2, 2, 7);
        gui.drawTexturedModalRect(8, yPos + 2, 15, 2, 30, 6);
        yPos += 8;

        gui.drawTexturedModalRect(0, yPos, 15, 98, 40, 2);

        return 25;
    }

    @Override
    public void recalculateComponentPositions() {
        super.recalculateComponentPositions();

        FollowingSlot followingSlot = getFollowingSlot();

        if (followingSlot != null) {
            followingSlot.setStartX(10);
            followingSlot.setEndX(followingSlot.getWidth() + 10);

            width = Math.max(width, followingSlot.getWidth() + 10);
        }

        if (hasParent()) {
            getParent().recalculateComponentPositions();
        }
    }

    @Override
    public Statement toStatement() {
        return new StmtHead(getFollowingStatement());
    }
}
