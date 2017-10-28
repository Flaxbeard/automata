package flaxbeard.automata.client.gui.codeblock.component;

import flaxbeard.automata.client.gui.GuiProgrammer;
import flaxbeard.automata.client.gui.codeblock.CodeBlockInstance;
import flaxbeard.automata.client.gui.codeblock.CodeBlockStatement;

public class FollowingSlot extends BlockSlot {

    @Override
    public boolean isBlockValid(CodeBlockInstance block) {
        return block.getBlock() instanceof CodeBlockStatement;
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

    @Override
    public Component clone() {
        return new FollowingSlot();
    }

}
