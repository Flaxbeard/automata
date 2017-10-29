package flaxbeard.automata.common.codeblock.component;

import flaxbeard.automata.client.gui.GuiProgrammer;
import flaxbeard.automata.common.codeblock.base.CodeBlock;

public abstract class Component {

    protected CodeBlock block;

    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public abstract int getWidth();
    public abstract int getHeight();
    public void drawForeground(GuiProgrammer gui) {}
    public void drawBackground(GuiProgrammer gui) {}

    public void setBlock(CodeBlock block) {
        this.block = block;
    }

    public CodeBlock getBlock() {
        return block;
    }

    public int getStartX() {
        return startX;
    }

    public int getEndX() {
        return endX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndY() {
        return endY;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }

    public void setEndY(int endY) {
        this.endY = endY;
    }

    public boolean isWithinBounds(int x, int y) {
        return (x >= getStartX() && x <= getEndX())
                && (y >= getStartY() && y <= getEndY());
    }
}