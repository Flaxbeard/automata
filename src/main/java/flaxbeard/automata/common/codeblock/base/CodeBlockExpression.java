package flaxbeard.automata.common.codeblock.base;

import flaxbeard.automata.client.gui.GuiProgrammer;
import flaxbeard.automata.common.codeblock.Type;
import flaxbeard.automata.common.codeblock.component.Component;
import flaxbeard.automata.common.program.base.Expression;

public abstract class CodeBlockExpression extends CodeBlock {

    public CodeBlockExpression(Component... components) {
        super(components);
    }

    public Type getType() {
        for (Type type : Type.values()) {
            if (canTakeType(type)) {
                return type;
            }
        }
        return null;
    }

    public abstract boolean canTakeType(Type type);

    public abstract <T> Expression<T> toExpression(Type<T> type);

    @Override
    protected int drawLeftEdge(GuiProgrammer gui) {
        if (getType() == Type.BOOLEAN) {
            for (int i = 1; i < getRenderHeight() - 1; i++) {
                float percentage = i / (getRenderHeight() - 1.0f);
                int type = (int) (percentage * 10);

                if (type < 5) {
                    gui.drawTexturedModalRect(0, i, 5, type, 5, 1);
                } else {
                    gui.drawTexturedModalRect(0, i, 0, (type - 5) + 99, 5, 1);
                }
            }

            return 5;
        }
        return super.drawLeftEdge(gui);
    }


    @Override
    protected int drawRightEdge(GuiProgrammer gui) {
        if (getType() == Type.BOOLEAN) {
            for (int i = 1; i < getRenderHeight() - 1; i++) {
                float percentage = i / (getRenderHeight() - 1.0f);
                int type = (int) (percentage * 10);

                if (type < 5) {
                    gui.drawTexturedModalRect(getRenderWidth() - 5, i, 1, type, 4, 1);
                } else {
                    gui.drawTexturedModalRect(getRenderWidth() - 5, i, 6, (type - 5) + 99, 4, 1);
                }
            }
            return 5;
        }
        return super.drawRightEdge(gui);
    }
}
