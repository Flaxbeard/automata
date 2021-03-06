package flaxbeard.automata.common.codeblock.component;

import flaxbeard.automata.client.gui.GuiProgrammer;
import flaxbeard.automata.common.codeblock.base.CodeBlock;
import flaxbeard.automata.common.codeblock.base.CodeBlockStatement;

public class FollowingSlot extends BlockSlot {

    @Override
    public boolean isBlockValid(CodeBlock block) {
        return block instanceof CodeBlockStatement
                && ((CodeBlockStatement) block).hasTopConnection();
    }

    @Override
    public void drawBackground(GuiProgrammer gui) {
        if (contents != null) {
            contents.drawBackground(gui);
        }
    }

    @Override
    public int getWidth() {
        if (contents == null) {
            return 50;
        }
        return contents.getWidth();
    }

    @Override
    public int getHeight() {
        if (contents == null) {
            return 9;
        }
        return contents.getHeight();
    }

}
