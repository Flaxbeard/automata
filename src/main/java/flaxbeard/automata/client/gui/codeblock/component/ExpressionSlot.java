package flaxbeard.automata.client.gui.codeblock.component;

import flaxbeard.automata.client.gui.codeblock.base.CodeBlock;
import flaxbeard.automata.client.gui.codeblock.base.CodeBlockExpression;
import flaxbeard.automata.client.gui.codeblock.Type;

public class ExpressionSlot extends BlockSlot {

    private final Type type;

    public ExpressionSlot(Type type) {
        this.type = type;
    }
    
    @Override
    public boolean isBlockValid(CodeBlock block) {
        if (block instanceof CodeBlockExpression) {
            return ((CodeBlockExpression) block).canTakeType(type);
        }
        return false;
    }
}
