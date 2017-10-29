package flaxbeard.automata.common.codeblock.component;

import flaxbeard.automata.common.codeblock.base.CodeBlock;
import flaxbeard.automata.common.codeblock.base.CodeBlockExpression;
import flaxbeard.automata.common.codeblock.Type;

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
