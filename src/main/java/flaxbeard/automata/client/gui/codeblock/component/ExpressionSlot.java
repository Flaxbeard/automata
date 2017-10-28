package flaxbeard.automata.client.gui.codeblock.component;

import flaxbeard.automata.client.gui.codeblock.CodeBlockInstance;
import flaxbeard.automata.client.gui.codeblock.CodeBlockExpression;
import flaxbeard.automata.client.gui.codeblock.Type;

public class ExpressionSlot extends BlockSlot {

    private final Type type;

    public ExpressionSlot(Type type) {
        this.type = type;
    }
    
    @Override
    public boolean isBlockValid(CodeBlockInstance blockInstance) {
        if (blockInstance.getBlock() instanceof CodeBlockExpression) {
            return ((CodeBlockExpression) blockInstance.getBlock()).canTakeType(blockInstance, type);
        }
        return false;
    }

    @Override
    public Component clone() {
        return new ExpressionSlot(type);
    }
}
